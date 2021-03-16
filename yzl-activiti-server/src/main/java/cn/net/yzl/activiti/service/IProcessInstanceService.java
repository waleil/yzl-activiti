package cn.net.yzl.activiti.service;

import cn.net.yzl.common.entity.ComResponse;

public interface IProcessInstanceService {

    /**
     * 挂起流程
     * @param processId
     * @return
     */
    ComResponse suspendProcessInstance(String processId);

    /**
     * 激活流程
     * @param processId
     * @return
     */
    ComResponse resumeProcessInstance(String processId);
}
