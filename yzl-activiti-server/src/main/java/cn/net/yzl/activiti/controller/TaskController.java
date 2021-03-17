package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.service.IActTaskService;
import cn.net.yzl.activiti.utils.AjaxResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Api(tags = "activiti调用任务接口")
@Slf4j
@RestController
public class TaskController{

    @Autowired
    private IActTaskService actTaskService;


    /**
     * 获取我的代办任务
     */
    @ApiOperation(value = "获取我的代办任务", notes = "获取我的代办任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", paramType = "query", dataType = "String", required = true, value = "登录用户"),
    })
    @GetMapping(value = "/getTasks/{userName}")
    public AjaxResponse getTasks(@PathVariable("userName") String userName) {
         log.info("获取我的代办任务,userName：【{}】", userName);
         return actTaskService.getTasks(userName);
    }


    /**
     * 完成待办任务
     */
    @ApiOperation(value = "完成待办任务", notes = "完成待办任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", paramType = "query", dataType = "String", required = true, value = "登录用户"),
            @ApiImplicitParam(name = "taskID", paramType = "query", dataType = "String", required = true, value = "任务ID"),
    })
    @GetMapping(value = "/completeTask/{userName}/{taskID}")
    public AjaxResponse completeTask(@PathVariable("userName") String userName,
                                 @PathVariable("taskID") String taskID) {
        log.info("完成待办任务,入参userName：【{}】,taskID：【{}】", userName,taskID);
        return actTaskService.completeTask(userName,taskID);
    }

}
