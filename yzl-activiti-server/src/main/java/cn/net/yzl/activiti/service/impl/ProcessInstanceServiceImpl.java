package cn.net.yzl.activiti.service.impl;

import cn.net.yzl.activiti.service.IProcessInstanceService;
import cn.net.yzl.common.entity.ComResponse;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessInstanceServiceImpl implements IProcessInstanceService {
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public ComResponse suspendProcessInstance(String processId) {
        runtimeService.suspendProcessInstanceById(processId);
        return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
    }

    @Override
    public ComResponse resumeProcessInstance(String processId) {
        runtimeService.activateProcessInstanceById(processId);
        return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
    }
}
