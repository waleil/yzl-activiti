package cn.net.yzl.activiti.service.impl;

import cn.net.yzl.activiti.dao.YzlBpmnDetailDAO;
import cn.net.yzl.activiti.domain.entity.*;
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
import org.springframework.beans.BeanUtils;
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
    private YzlBpmnDetailDAO yzlBpmnDetailDAO;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ComResponse fileUpload(MultipartFile multipartFile, CreateProcessVO createProcessVO) {
        try {
            //todo: 路径linux上设置 nacos上配置
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

            YzlBpmnDetail yzlBpmnDetail = new YzlBpmnDetail();

            BeanUtils.copyProperties(createProcessVO, yzlBpmnDetail);
            yzlBpmnDetail.setFileName(fileName);
            yzlBpmnDetail.setFilePath(filePath);
            yzlBpmnDetail.setFileStatus(FileStatusEnum.NORMAL.getCode().byteValue());
            yzlBpmnDetail.setFileType(FileTypeEnum.matcher(suffixName).getCode().byteValue());
            yzlBpmnDetail.setCreateTime(new Date());
            yzlBpmnDetail.setCreater(createProcessVO.getUserName());
            yzlBpmnDetailDAO.insertSelective(yzlBpmnDetail);
            return ComResponse.success(ComResponse.SUCCESS_STATUS, null, "成功", yzlBpmnDetail.getId());
        } catch (Exception e) {
            log.error("流程上传失败, 失败原因：{}", e.getStackTrace());
            e.printStackTrace();
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }

    @Override
    public ComResponse<List<ProcessDefinitionDTO>> getProcessDefinitionList() {
        try {
            //todo : 需要优化
            List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();

            YzlBpmnDetailExample yzlBpmnDetailExample = new YzlBpmnDetailExample();
            YzlBpmnDetailExample.Criteria criteria = yzlBpmnDetailExample.createCriteria();
            criteria.andDeletedEqualTo(DeletedEnum.DELETED.getCode().byteValue());
            List<YzlBpmnDetail> yzlBpmnDetails = yzlBpmnDetailDAO.selectByExample(yzlBpmnDetailExample);
            List<String> processIds = new ArrayList<>();
            if (!yzlBpmnDetails.isEmpty()) {
                processIds = yzlBpmnDetails.stream().map(YzlBpmnDetail::getProcessId).collect(Collectors.toList());
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
            YzlBpmnDetail yzlBpmnDetail = yzlBpmnDetailDAO.selectByPrimaryKey(fileId);
            File file = new File(yzlBpmnDetail.getFilePath());
            FileInputStream fis = new FileInputStream(file);
            //input stream方式
            Deployment deploy = repositoryService.createDeployment()
                    .name(yzlBpmnDetail.getProcessName())
                    .addInputStream(yzlBpmnDetail.getProcessName() + ".bpmn", fis)
                    .key(yzlBpmnDetail.getProcessKey())
                    .deploy();

            yzlBpmnDetail.setProcessId(deploy.getId());
            yzlBpmnDetailDAO.updateByPrimaryKey(yzlBpmnDetail);

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
            YzlBpmnDetailExample yzlBpmnDetailExample = new YzlBpmnDetailExample();
            YzlBpmnDetailExample.Criteria criteria = yzlBpmnDetailExample.createCriteria();
            criteria.andProcessIdEqualTo(processId);

            List<YzlBpmnDetail> yzlBpmnDetails = yzlBpmnDetailDAO.selectByExample(yzlBpmnDetailExample);
            if (yzlBpmnDetails.isEmpty()) {
                log.error("processId:【{}】对应流程不存在", processId);
                return new ComResponse().setMessage("processId对应流程不存在").setCode(ComResponse.ERROR_STATUS);
            }
            YzlBpmnDetail yzlBpmnDetail = yzlBpmnDetails.get(0);
            yzlBpmnDetail.setDeleted(DeletedEnum.DELETED.getCode().byteValue());
            yzlBpmnDetailDAO.updateByPrimaryKey(yzlBpmnDetail);
//            repositoryService.deleteDeployment(id);
            return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("删除流程定义【{}】失败, 失败原因：{}", processId, e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }
}
