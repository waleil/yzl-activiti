package cn.net.yzl.activiti.service;

import cn.net.yzl.activiti.model.vo.CreateProcessVO;
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
     * @param createProcessVO
     */
    ComResponse fileUpload(MultipartFile multipartFile, CreateProcessVO createProcessVO);

    /**
     * 查询流程
     * @return
     */
    ComResponse getProcessDefinitionList();

    /**
     * 发布流程
     * @param fileId
     * @return
     */
    ComResponse pushProcessDefinition(Long fileId);
}
