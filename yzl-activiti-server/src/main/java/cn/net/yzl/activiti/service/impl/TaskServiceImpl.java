package cn.net.yzl.activiti.service.impl;

import cn.net.yzl.activiti.model.dto.UserTaskDTO;
import cn.net.yzl.activiti.service.ITaskService;
import cn.net.yzl.common.entity.ComResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl implements ITaskService {
    @Autowired
    private TaskService taskService;


    @Override
    public ComResponse getTask(String userName) {
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            List<Task> tasks = processEngine.getTaskService()//与任务相关的Service
                    .createTaskQuery()//创建一个任务查询对象
                    .taskAssignee(userName)
                    .list();
            if (tasks.isEmpty()) {
                log.error("【{}】当前没有任务", userName);
                return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
            }
            List<UserTaskDTO> collect = tasks.stream().map(task -> {
                UserTaskDTO userTaskDTO = new UserTaskDTO();
                BeanUtils.copyProperties(task, userTaskDTO);
                return userTaskDTO;
            }).collect(Collectors.toList());


            return ComResponse.success(collect);
        } catch (Exception e) {
            log.error("【{}】获取任务列表失败，失败原因：{}", userName, e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }

    @Override
    public ComResponse pass(String taskId) {
        try {
            taskService.complete(taskId, null, false);
            return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【{}】完成任务，失败原因：{}", taskId, e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }
}
