package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.model.dto.ProcessDefinitionDTO;
import cn.net.yzl.activiti.model.vo.CreateProcessVO;
import cn.net.yzl.activiti.service.IProcessDefinitionService;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.model.dto.ActivitiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 流程管理控制层
 */
@RestController
public class ProcessDefinitionController {

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    /**
     * 创建流程
     * @return
     */
    @PostMapping(value = "/process")
    public ComResponse fileUpload(@RequestParam("multipartFile") MultipartFile multipartFile, CreateProcessVO createProcessVO) {
        return processDefinitionService.fileUpload(multipartFile, createProcessVO);
    }

    /**
     * 查询模型列表
     * @return
     */
    @GetMapping(value = "/process/definitions")
    public ComResponse<List<ProcessDefinitionDTO>> getProcessDefinitionList() {
        return processDefinitionService.getProcessDefinitionList();
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
    @Deprecated
    public ComResponse delProcessDefinition(@PathVariable("id") String id) {
        return processDefinitionService.delProcessDefinition(id);
    }

    /**
     * 模型发布
     * @return
     */
    @GetMapping(value = "/process/start/{fileId}")
    public ComResponse pushProcessDefinition(@PathVariable("fileId") Long fileId) {
        return processDefinitionService.pushProcessDefinition(fileId);
    }
}
