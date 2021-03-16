package cn.net.yzl.activiti.service.impl;

import cn.net.yzl.activiti.dao.ActBpmnFileDAO;
import cn.net.yzl.activiti.domain.entity.ActBpmnFile;
import cn.net.yzl.activiti.domain.entity.ActBpmnFileExample;
import cn.net.yzl.activiti.service.IProcessInstanceService;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


@Slf4j
@Service
public class ProcessInstanceServiceImpl implements IProcessInstanceService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActBpmnFileDAO actBpmnFileDAO;

    @Override
    public ComResponse processDetail(String processId) {

        ActBpmnFileExample actBpmnFileExample = new ActBpmnFileExample();
        ActBpmnFileExample.Criteria criteria = actBpmnFileExample.createCriteria();
        criteria.andProcessIdEqualTo(processId);

        List<ActBpmnFile> actBpmnFiles = actBpmnFileDAO.selectByExample(actBpmnFileExample);
        if (!actBpmnFiles.isEmpty()) {
            return ComResponse.success(actBpmnFiles.get(0));
        }
        log.error("processId:{} 没有查到对应信息", processId);
        return ComResponse.fail(ResponseCodeEnums.NO_DATA);
    }

    @Override
    public void processXmlDetail(HttpServletResponse response, String processId, String processName) {
        InputStream inputStream = repositoryService.getResourceAsStream(processId, processName);
        try {
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
        } catch (Exception e) {
            log.error("获取流程文件失败，失败原因：{}", e.getStackTrace());
        }
    }

    @Override
    public ComResponse suspendProcessInstance(String processId) {
        try {
            runtimeService.suspendProcessInstanceById(processId);
            return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("挂起流程失败，失败原因：{}", e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }

    @Override
    public ComResponse resumeProcessInstance(String processId) {
        try {
            runtimeService.activateProcessInstanceById(processId);
            return new ComResponse().setCode(ComResponse.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("激活流程失败，失败原因：{}", e.getStackTrace());
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }
}
