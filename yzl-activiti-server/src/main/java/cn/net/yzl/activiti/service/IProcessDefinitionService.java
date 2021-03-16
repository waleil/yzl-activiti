package cn.net.yzl.activiti.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.dto.ActivitiResult;

public interface IProcessDefinitionService {
    ComResponse delProcessDefinition(String id);
}
