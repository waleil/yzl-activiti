package cn.net.yzl.activiti.service.impl;

import cn.net.yzl.activiti.dao.ActBpmnFileDAO;
import cn.net.yzl.activiti.domain.entity.ActBpmnFile;
import cn.net.yzl.activiti.enums.FileStatusEnum;
import cn.net.yzl.activiti.enums.FileTypeEnum;
import cn.net.yzl.activiti.service.IProcessDefinitionService;
import cn.net.yzl.common.entity.ComResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProcessDefinitionServiceImpl implements IProcessDefinitionService {

    @Resource
    private RepositoryService repositoryService;
    @Autowired
    private ActBpmnFileDAO actBpmnFileDAO;

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

    @Override
    public ComResponse fileUpload(MultipartFile multipartFile, String userName) {
        try {
            // 文件名
            String fileName = multipartFile.getOriginalFilename();
            // 后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 上传后的路径
            String filePath = "/processes/";

            //本地路径格式转上传路径格式
            filePath = filePath.replace("\\", "/");
            // 新文件名
            fileName = UUID.randomUUID() + suffixName;
            File file = new File(filePath + fileName);
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
            actBpmnFile.setCreater(userName);
            int sum = actBpmnFileDAO.insertSelective(actBpmnFile);
            if (sum > 0) {
                return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
            }
            log.error("流程上传失败, 插入数据库失败");
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        } catch (Exception e) {
            log.error("流程上传失败, 失败原因：{}", e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }

    @Override
    public ComResponse getProcessDefinitionList() {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
        return ComResponse.success(processDefinitionList);
    }
}
