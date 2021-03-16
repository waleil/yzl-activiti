package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.config.SecurityUtil;
import cn.net.yzl.activiti.service.IProcessDefinitionService;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.model.dto.ActivitiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 流程管理控制层
 */
@RestController
public class ProcessDefinitionController {

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    /**
     * 文件上传bpmn
     * @return
     */
    @PostMapping(value = "/upload")
    public ComResponse fileUpload(@RequestParam("processFile") MultipartFile multipartFile, @RequestParam("userName") String userName) {
        if (multipartFile.isEmpty()) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE, "上传文件不能为空");
        }
        return processDefinitionService.fileUpload(multipartFile, userName);
    }

    /**
     * 查询模型列表
     * @return
     */
    @GetMapping(value = "/process/definitions")
    public ComResponse getProcessDefinitionList() {
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
