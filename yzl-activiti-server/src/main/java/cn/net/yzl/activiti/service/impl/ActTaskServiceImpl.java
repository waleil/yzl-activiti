package cn.net.yzl.activiti.service.impl;

import cn.net.yzl.activiti.config.SecurityUtil;
import cn.net.yzl.activiti.model.vo.RejectedVO;
import cn.net.yzl.activiti.utils.AjaxResponse;
import cn.net.yzl.activiti.utils.GlobalConfig;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.dto.ActivitiResult;
import cn.net.yzl.activiti.service.IActTaskService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 任务处理层
 * @author yuhongbo
 */
@Slf4j
@Service
public class ActTaskServiceImpl implements IActTaskService {


    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskService taskService;

    @Override
    public ComResponse pass(String userName,String taskId) {
        try {
            securityUtil.logInAs(userName);
            Task task = taskRuntime.task(taskId);
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            }
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId())
                    //.withVariable("num", "2")//执行环节设置变量
                    .build());
            return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【{}】完成任务，失败原因：{}", taskId, e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }

    @Override
    public AjaxResponse getTasks(String userName) {
        try {
//            securityUtil.logInAs(userName);
            securityUtil.logInAs(userName);
            org.activiti.api.runtime.shared.query.Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 100));
            List<HashMap<String, Object>> listMap = new ArrayList<>();
            for (Task tk : tasks.getContent()) {
                ProcessInstance processInstance = processRuntime.processInstance(tk.getProcessInstanceId());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", tk.getId());
                hashMap.put("name", tk.getName());
                hashMap.put("status", tk.getStatus());
                hashMap.put("createdDate", tk.getCreatedDate());
                if (tk.getAssignee() == null) {//执行人，null时前台显示未拾取
                    hashMap.put("assignee", "待拾取任务");
                } else {
                    hashMap.put("assignee", tk.getAssignee());//
                }
                hashMap.put("instanceName", processInstance.getName());
                listMap.add(hashMap);
            }
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), listMap);
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取我的代办任务失败", e.toString());
        }
    }

    @Override
    public ComResponse rejected(RejectedVO rejectedVO) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("dealUser", rejectedVO.getUserName());
            variables.put("dealReason",rejectedVO.getDealReason());
            taskService.complete(rejectedVO.getTaskId(), variables);
            return ComResponse.success();
        } catch (Exception e) {
            log.error("拒绝失败， 失败原因： {}", e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }

}
