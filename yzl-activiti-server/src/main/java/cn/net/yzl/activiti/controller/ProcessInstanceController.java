package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.service.IProcessInstanceService;
import cn.net.yzl.common.entity.ComResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessInstanceController {

    @Autowired
    private IProcessInstanceService processInstanceService;


    /**
     * 流程实例列表
     * @return
     */
    public ComResponse processInstanceList() {
        return null;
    }

    /**
     * 挂起流程实例
     * @return
     */
    @GetMapping(value = "/process/suspend/{processId}")
    public ComResponse suspendProcessInstance(@RequestParam("processId") String processId) {
        return processInstanceService.suspendProcessInstance(processId);
    }

    /**
     * 激活流程实例
     * @return
     */
    @GetMapping(value = "/process/resume/{processId}")
    public ComResponse resumeProcessInstance(@RequestParam("processId") String processId) {
        return processInstanceService.resumeProcessInstance(processId);
    }

    /**
     * 删除流程实例
     * @return
     */
    public ComResponse delProcessInstance() {
        return null;
    }

}
