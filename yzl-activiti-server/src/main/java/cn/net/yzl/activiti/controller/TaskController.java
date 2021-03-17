package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.service.ITaskService;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.dto.ActivitiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
    @Autowired
    private ITaskService taskService;


    @GetMapping(value = "/{userName}")
    public ComResponse getTask(@PathVariable("userName") String userName) {
        return taskService.getTask(userName);
    }

    /**
     * 审批通过
     * @return
     */
    @GetMapping(value = "/pass/{taskId}")
    public ComResponse pass(@PathVariable("taskId") String taskId) {
        return taskService.pass(taskId);
    }

    /**
     * 拒绝
     * @return
     */
    public ActivitiResult refuse() {
        return null;
    }
}
