package cn.net.yzl.activiti.service.impl;

import cn.net.yzl.activiti.service.IProcessDefinitionService;
import cn.net.yzl.common.entity.ComResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ProcessDefinitionServiceImpl implements IProcessDefinitionService {

    @Resource
    private RepositoryService repositoryService;

    @Override
    public ComResponse delProcessDefinition(String id) {
        try {
            repositoryService.deleteDeployment(id);
            return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("删除流程定义【{}】失败, 失败原因：{}", id, e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }
}
