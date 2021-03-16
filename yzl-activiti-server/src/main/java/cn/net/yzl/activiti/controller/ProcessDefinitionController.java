package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.service.IProcessDefinitionService;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.dto.ActivitiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程管理控制层
 */
@RestController
public class ProcessDefinitionController {

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    /**
     * 保存模型
     * @return
     */
    public ActivitiResult createProcessDefinition() {
        return null;
    }

    /**
     * 文件上传模型
     * @return
     */
    public ActivitiResult fileUpload() {
        return null;
    }

    /**
     * 查询模型列表
     * @return
     */
    public ActivitiResult getProcessDefinitionList() {
        return null;
    }

    /**
     * 查询模型详情
     * @return
     */
    public ActivitiResult getProcessDefinition() {
        return null;
    }

    /**
     * 删除模型
     * @return
     */
    @DeleteMapping("/{id}")
    public ComResponse delProcessDefinition(@PathVariable("id") String id) {
        return processDefinitionService.delProcessDefinition(id);
    }

    /**
     * 模型发布
     * @return
     */
    public ActivitiResult pushProcessDefinition() {
        return null;
    }
}
