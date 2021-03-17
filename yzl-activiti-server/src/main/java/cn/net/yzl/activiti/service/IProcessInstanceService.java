package cn.net.yzl.activiti.service;

import cn.net.yzl.common.entity.ComResponse;

import javax.servlet.http.HttpServletResponse;

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

    /**
     * 查询流程
     * @param processId
     * @return
     */
    ComResponse processDetail(String processId);

    /**
     * 查询流程xml
     * @param response
     * @param processId
     * @param processName
     */
    void processXmlDetail(HttpServletResponse response, String processId, String processName);

    /**
     * 启动流程
     * @param processDefinitionKey
     * @param instanceName
     * @param instanceVariable
     * @return
     */
    ComResponse startProcess(String processDefinitionKey, String instanceName, String instanceVariable);
}
