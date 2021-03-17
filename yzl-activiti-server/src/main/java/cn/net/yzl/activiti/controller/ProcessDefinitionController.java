package cn.net.yzl.activiti.controller;

import cn.net.yzl.activiti.model.dto.ProcessDefinitionDTO;
import cn.net.yzl.activiti.model.vo.CreateProcessVO;
import cn.net.yzl.activiti.service.IProcessDefinitionService;
import cn.net.yzl.common.entity.ComResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 流程管理控制层
 */
@Api(tags = "activiti对应流程实例接口")
@Slf4j
@RestController
public class ProcessDefinitionController {

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    /**
     * 查询流程列表
     * @return
     */
    @ApiOperation(value = "查询流程列表", notes = "查询流程列表")
    @GetMapping(value = "/process/definitions")
    public ComResponse<List<ProcessDefinitionDTO>> getProcessDefinitionList() {
        return processDefinitionService.getProcessDefinitionList();
    }

    /**
     * 创建流程
     * @return
     */
    @ApiOperation(value = "创建流程", notes = "创建流程")
    @PostMapping(value = "/process")
    public ComResponse fileUpload(@RequestParam("multipartFile") MultipartFile multipartFile, CreateProcessVO createProcessVO) {
        return processDefinitionService.fileUpload(multipartFile, createProcessVO);
    }

    /**
     * 删除流程
     * @return
     */
    @ApiOperation(value = "删除流程", notes = "删除流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", paramType = "query", dataType = "String", required = true, value = "流程id"),
    })
    @DeleteMapping("/delete/{processId}")
    @Deprecated
    public ComResponse delProcessDefinition(@PathVariable("processId") String processId) {
        return processDefinitionService.delProcessDefinition(processId);
    }

    /**
     * 流程发布
     * @return
     */
    @ApiOperation(value = "流程发布", notes = "流程发布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", paramType = "query", dataType = "String", required = true, value = "上传文件对应id"),
    })
    @PostMapping(value = "/process/start/{fileId}")
    public ComResponse pushProcessDefinition(@PathVariable("fileId") Long fileId) {
        return processDefinitionService.pushProcessDefinition(fileId);
    }
}
