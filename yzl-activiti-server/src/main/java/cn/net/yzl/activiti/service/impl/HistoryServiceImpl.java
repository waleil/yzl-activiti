package cn.net.yzl.activiti.service.impl;

import cn.net.yzl.activiti.service.IHistoryService;
import cn.net.yzl.common.entity.ComResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HistoryServiceImpl implements IHistoryService {
    @Override
    public ComResponse historyDetail(String processInstanceId) {
        try {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            List<HistoricActivityInstance> list = engine.getHistoryService()
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .list();
            return ComResponse.success(list);
        } catch (Exception e) {
            log.error("【{}】获取流程历史明细失败", processInstanceId);
            return new ComResponse().setCode(ComResponse.ERROR_STATUS);
        }
    }
}
