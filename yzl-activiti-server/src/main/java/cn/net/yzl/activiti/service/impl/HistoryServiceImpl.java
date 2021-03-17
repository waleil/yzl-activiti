package cn.net.yzl.activiti.service.impl;

import cn.net.yzl.activiti.dao.HistoryInfoDAO;
import cn.net.yzl.activiti.service.IHistoryService;
import cn.net.yzl.common.entity.ComResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HistoryServiceImpl implements IHistoryService {
    @Autowired
    private HistoryInfoDAO historyInfoDAO;
    @Autowired
    private HistoryService historyService;

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

    @Override
    public ComResponse myCreated(String userName) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> hiPros = historyInfoDAO.selectMyProcessStarted(userName);
        setVariables(list, hiPros);
        return ComResponse.success(hiPros);
    }

    @Override
    public ComResponse myApproved(String userName) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> hiTasks = historyInfoDAO.selectMyTasksCompleted(userName);
        setVariables(list, hiTasks);
        return ComResponse.success(hiTasks);
    }

    private void setVariables(List<Map<String, Object>> listNew, List<Map<String, Object>> listOld) {
        if (!CollectionUtils.isEmpty(listOld)) {
            for (Map<String, Object> hipro : listOld) {
                List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery().
                        processInstanceId((String) hipro.get("PROC_INST_ID_")).list();
                if (!CollectionUtils.isEmpty(variables)) {
                    for (HistoricVariableInstance variable : variables) {
                        hipro.put(variable.getVariableName(), variable.getValue());
                    }
                }
                listNew.add(hipro);
            }
        }
    }

}
