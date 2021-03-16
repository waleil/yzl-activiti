package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.model.vo.CreateProcessVO;
import cn.net.yzl.activiti.service.IProcessInstanceService;
import cn.net.yzl.common.entity.ComResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ProcessInstanceController {

    @Autowired
    private IProcessInstanceService processInstanceService;

    /**
     * 查询流程xml
     * @return
     */
    @GetMapping(value = "/process/xml/{processId}/{processName}")
    public void processXmlDetail(HttpServletResponse response, @PathVariable("processId") String processId,
                                     @PathVariable("processName") String processName) {
        processInstanceService.processXmlDetail(response, processId, processName);
    }

    /**
     * 查看流程详情
     * @return
     */
    @GetMapping(value = "/process/{processId}")
    public ComResponse processDetail(@PathVariable("processId") String processId) {
        return processInstanceService.processDetail(processId);
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

}
