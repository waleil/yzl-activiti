package cn.net.yzl.activiti.controller;

import cn.net.yzl.model.dto.ActivitiResult;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessInstanceController {

    /**
     * 流程实例列表
     * @return
     */
    public ActivitiResult processInstanceList() {
        return null;
    }

    /**
     * 挂起流程实例
     * @return
     */
    public ActivitiResult suspendProcessInstance() {
        return null;
    }

    /**
     * 激活流程实例
     * @return
     */
    public ActivitiResult resumeProcessInstance() {
        return null;
    }

    /**
     * 删除流程实例
     * @return
     */
    public ActivitiResult delProcessInstance() {
        return null;
    }

}
