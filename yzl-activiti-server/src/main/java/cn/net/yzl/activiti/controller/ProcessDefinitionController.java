package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.model.dto.ProcessDefinitionDTO;
import cn.net.yzl.activiti.model.vo.CreateProcessVO;
import cn.net.yzl.activiti.service.IProcessDefinitionService;
import cn.net.yzl.common.entity.ComResponse;
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
     * 查询流程列表
     * @return
     */
    @GetMapping(value = "/process/definitions")
    public ComResponse<List<ProcessDefinitionDTO>> getProcessDefinitionList() {
        return processDefinitionService.getProcessDefinitionList();
    }

    /**
     * 创建流程
     * @return
     */
    @PostMapping(value = "/process")
    public ComResponse fileUpload(@RequestParam("multipartFile") MultipartFile multipartFile, CreateProcessVO createProcessVO) {
        return processDefinitionService.fileUpload(multipartFile, createProcessVO);
    }

    /**
     * 删除流程
     * @return
     */
    @DeleteMapping("/{id}")
    @Deprecated
    public ComResponse delProcessDefinition(@PathVariable("id") String id) {
        return processDefinitionService.delProcessDefinition(id);
    }

    /**
     * 流程发布
     * @return
     */
    @PostMapping(value = "/process/start/{fileId}")
    public ComResponse pushProcessDefinition(@PathVariable("fileId") Long fileId) {
        return processDefinitionService.pushProcessDefinition(fileId);
    }
}
