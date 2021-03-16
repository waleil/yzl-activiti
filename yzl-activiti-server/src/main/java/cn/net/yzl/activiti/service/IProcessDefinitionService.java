package cn.net.yzl.activiti.service;

import cn.net.yzl.common.entity.ComResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IProcessDefinitionService {
    /**
     * 删除流程
     * @param id
     * @return
     */
    ComResponse delProcessDefinition(String id);

    /**
     * 流程图上传
     * @param multipartFile
     * @param userName
     */
    ComResponse fileUpload(MultipartFile multipartFile, String userName);

    /**
     * 查询流程
     * @return
     */
    ComResponse getProcessDefinitionList();

}
