package cn.net.yzl.activiti.service.impl;

import cn.net.yzl.activiti.dao.ActBpmnFileDAO;
import cn.net.yzl.activiti.domain.entity.ActBpmnFile;
import cn.net.yzl.activiti.domain.entity.ActBpmnFileExample;
import cn.net.yzl.activiti.enums.DeletedEnum;
import cn.net.yzl.activiti.enums.FileStatusEnum;
import cn.net.yzl.activiti.enums.FileTypeEnum;
import cn.net.yzl.activiti.model.dto.ProcessDefinitionDTO;
import cn.net.yzl.activiti.model.vo.CreateProcessVO;
import cn.net.yzl.activiti.service.IProcessDefinitionService;
import cn.net.yzl.common.entity.ComResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 审批流程服务层
 * @author limisheng
 * @date 2021/03/16
 */
@Slf4j
@Service
public class ProcessDefinitionServiceImpl implements IProcessDefinitionService {

    @Resource
    private RepositoryService repositoryService;
    @Autowired
    private ActBpmnFileDAO actBpmnFileDAO;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ComResponse fileUpload(MultipartFile multipartFile, CreateProcessVO createProcessVO) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            // 后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            String programPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            // 上传后的路径
            String filePath = "processes/";

            //本地路径格式转上传路径格式
            filePath = filePath.replace("\\", "/");
            // 新文件名
            fileName = UUID.randomUUID() + suffixName;
            filePath = programPath + filePath + fileName;
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            multipartFile.transferTo(file);

            ActBpmnFile actBpmnFile = new ActBpmnFile();
            actBpmnFile.setFileName(fileName);
            actBpmnFile.setFilePath(filePath);
            actBpmnFile.setFileStatus(FileStatusEnum.NORMAL.getCode().byteValue());
            actBpmnFile.setFileType(FileTypeEnum.matcher(suffixName).getCode().byteValue());
            actBpmnFile.setCreateTime(new Date());
            actBpmnFile.setApprovalType(createProcessVO.getApprovalType());
            actBpmnFile.setEvent(createProcessVO.getEvent());
            actBpmnFile.setProcessName(createProcessVO.getProcessName());
            actBpmnFile.setCreater(createProcessVO.getUserName());
            Integer id = actBpmnFileDAO.insertSelective(actBpmnFile);
            return ComResponse.success(ComResponse.SUCCESS_STATUS, null, "成功", id);
        } catch (Exception e) {
            log.error("流程上传失败, 失败原因：{}", e.getStackTrace());
            e.printStackTrace();
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }

    @Override
    public ComResponse<List<ProcessDefinitionDTO>> getProcessDefinitionList() {
        try {
            List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();

            ActBpmnFileExample actBpmnFileExample = new ActBpmnFileExample();
            ActBpmnFileExample.Criteria criteria = actBpmnFileExample.createCriteria();
            criteria.andDeletedEqualTo(DeletedEnum.DELETED.getCode().byteValue());
            List<ActBpmnFile> actBpmnFiles = actBpmnFileDAO.selectByExample(actBpmnFileExample);
            List<String> processIds = new ArrayList<>();
            if (!actBpmnFiles.isEmpty()) {
                processIds = actBpmnFiles.stream().map(ActBpmnFile::getProcessId).collect(Collectors.toList());
            }

            List<ProcessDefinitionDTO> collect = new ArrayList<>();
            if (!CollectionUtils.isEmpty(processDefinitionList)) {
                List<String> finalProcessIds = processIds;
                collect = processDefinitionList.stream().filter(processDefinition -> !finalProcessIds.contains(processDefinition.getId()))
                        .map(processDefinition -> {
                            ProcessDefinitionDTO processDefinitionDTO = new ProcessDefinitionDTO();
                            processDefinitionDTO.setId(processDefinition.getDeploymentId());
                            processDefinitionDTO.setName(processDefinition.getName());
                            return processDefinitionDTO;
                        }).collect(Collectors.toList());
            }
            return ComResponse.success(collect);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取审批流程列表失败：{}", e.getStackTrace());
        }
        return ComResponse.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ComResponse pushProcessDefinition(Long fileId) {

        try {
            ActBpmnFile actBpmnFile = actBpmnFileDAO.selectByPrimaryKey(fileId);
            File file = new File(actBpmnFile.getFilePath());
            FileInputStream fis = new FileInputStream(file);
            //inputstream方式
            Deployment deploy = repositoryService.createDeployment()
                    .name(actBpmnFile.getProcessName())
                    .addInputStream(actBpmnFile.getProcessName(), fis)
                    .deploy();
            actBpmnFile.setProcessId(deploy.getId());
            actBpmnFileDAO.updateByPrimaryKey(actBpmnFile);

            return ComResponse.success(deploy);
        } catch (Exception e) {
            log.error("流程启动失败，失败原因：{}", e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ComResponse delProcessDefinition(String processId) {
        try {
            //todo: 校验当前是否存在审批流程
            ActBpmnFileExample actBpmnFileExample = new ActBpmnFileExample();
            ActBpmnFileExample.Criteria criteria = actBpmnFileExample.createCriteria();
            criteria.andProcessIdEqualTo(processId);

            List<ActBpmnFile> actBpmnFiles = actBpmnFileDAO.selectByExample(actBpmnFileExample);
            if (actBpmnFiles.isEmpty()) {
                log.error("processId:【{}】对应流程不存在", processId);
                return new ComResponse().setMessage("processId对应流程不存在").setCode(ComResponse.ERROR_STATUS);
            }
            ActBpmnFile actBpmnFile = actBpmnFiles.get(0);
            actBpmnFile.setDeleted(DeletedEnum.DELETED.getCode().byteValue());
            actBpmnFileDAO.updateByPrimaryKey(actBpmnFile);
//            repositoryService.deleteDeployment(id);
            return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("删除流程定义【{}】失败, 失败原因：{}", processId, e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }
}
