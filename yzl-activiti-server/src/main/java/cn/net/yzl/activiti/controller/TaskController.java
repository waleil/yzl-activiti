package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.service.ITaskService;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.dto.ActivitiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "activiti对应流程实例接口")
@RestController
public class TaskController {
    @Autowired
    private ITaskService taskService;

    @ApiOperation(value = "用户查询代办任务列表", notes = "用户查询代办任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", paramType = "query", dataType = "String", required = true, value = "用户名"),
    })
    @GetMapping(value = "/task/{userName}")
    public ComResponse getTask(@PathVariable("userName") String userName) {
        return taskService.getTask(userName);
    }

    /**
     * 审批通过
     * @return
     */
    @ApiOperation(value = "审批通过", notes = "审批通过")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", paramType = "query", dataType = "String", required = true, value = "任务id"),
    })
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
