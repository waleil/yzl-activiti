package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.service.IProcessInstanceService;
import cn.net.yzl.common.entity.ComResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "activiti对应流程实例接口")
@Slf4j
@RestController
public class ProcessInstanceController {

    @Autowired
    private IProcessInstanceService processInstanceService;

    /**
     * 查询流程xml
     * @return
     */
    @ApiOperation(value = "查询流程xml", notes = "查询流程xml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", paramType = "query", dataType = "String", required = true, value = "流程id"),
            @ApiImplicitParam(name = "processName", paramType = "query", dataType = "String", required = true, value = "流程名称"),
    })
    @GetMapping(value = "/process/xml/{processId}/{processName}")
    public void processXmlDetail(HttpServletResponse response, @PathVariable("processId") String processId,
                                     @PathVariable("processName") String processName) {
       log.info("查询流程xml,入参processId：【{}】, processName:【{}】", processId, processName);
       processInstanceService.processXmlDetail(response, processId, processName);
    }

    /**
     * 查看流程详情
     * @return
     */
    @ApiOperation(value = "查看流程详情", notes = "查看流程详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", paramType = "query", dataType = "String", required = true, value = "流程id"),
    })
    @GetMapping(value = "/process/{processId}")
    public ComResponse processDetail(@PathVariable("processId") String processId) {
        log.info("查看流程详情,processId：【{}】", processId);
        return processInstanceService.processDetail(processId);
    }

    /**
     * 挂起流程实例
     * @return
     */
    @ApiOperation(value = "挂起流程实例", notes = "挂起流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", paramType = "query", dataType = "String", required = true, value = "流程id"),
    })
    @GetMapping(value = "/process/suspend/{processId}")
    public ComResponse suspendProcessInstance(@PathVariable("processId") String processId) {
        log.info("挂起流程实例,processId：【{}】", processId);
        return processInstanceService.suspendProcessInstance(processId);
    }

    /**
     * 激活流程实例
     * @return
     */
    @ApiOperation(value = "激活流程实例", notes = "激活流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", paramType = "query", dataType = "String", required = true, value = "流程id"),
    })
    @GetMapping(value = "/process/resume/{processId}")
    public ComResponse resumeProcessInstance(@PathVariable("processId") String processId) {
        log.info("激活流程实例,processId：【{}】", processId);
        return processInstanceService.resumeProcessInstance(processId);
    }

    /**
     * 启动流程
     * @return
     */
    @ApiOperation(value = "启动流程", notes = "启动流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", paramType = "query", dataType = "String", required = true, value = "key"),
            @ApiImplicitParam(name = "instanceName", paramType = "query", dataType = "String", required = true, value = "实力名"),
            @ApiImplicitParam(name = "instanceVariable", paramType = "query", dataType = "String", required = true, value = "流程id"),
    })
    @GetMapping(value = "/process/start")
    public ComResponse startProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                    @RequestParam("instanceName") String instanceName,
                                    @RequestParam("instanceVariable") String instanceVariable) {
        log.info("启动流程,processDefinitionKey：【{}】, instanceName:【{}】, instanceVariable:【{}】",
                processDefinitionKey, instanceName, instanceVariable);
        return processInstanceService.startProcess(processDefinitionKey, instanceName, instanceVariable);
    }

}
