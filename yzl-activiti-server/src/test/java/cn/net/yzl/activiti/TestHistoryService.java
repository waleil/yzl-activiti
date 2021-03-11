package cn.net.yzl.activiti;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

/**
 * 历史service,可以查询所有历史数据,例如,流程实例,任务,活动，变量，附件等。
 * 如何查看历史信息 用户的历史记录 任务的历史记录
 * 涉及到哪些表
 * 如何操作
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestHistoryService {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 主要获取act_hi_procinst表中流程实例属性信息
     */
    @Test
    public void testRepositoryService() {
        // 获取流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = engine.getHistoryService();

        //根据processInstanceId 可以查询当前流程实例属性信息
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processInstanceId("30316d0a-8242-11eb-9771-18c04d47ad75").list();
        System.out.println("当前流程实例：" + JSON.toJSONString(list));

        //未完成任务
        List<HistoricProcessInstance> unfinishedList = historyService.createHistoricProcessInstanceQuery().unfinished().list();
        System.out.println("unfinishedList：" + JSON.toJSONString(unfinishedList));
        //已完成任务
        List<HistoricProcessInstance> finishedList = historyService.createHistoricProcessInstanceQuery().finished().list();
        System.out.println("finishedList：" + JSON.toJSONString(finishedList));
    }


    /**
     * 主要获取act_hi_taskinst表中历史任务属性信息
     */
    @Test
    public void testRepositoryService1() {
        // 获取流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = engine.getHistoryService();

        //根据processInstanceId 可以查询当前流程实例属性信息
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId("30316d0a-8242-11eb-9771-18c04d47ad75").list();
        System.out.println("根据流程id获取历史记录：" + JSON.toJSONString(list));

        //当前用户可以查看当前任务
        List<HistoricTaskInstance> zhangsan = historyService.createHistoricTaskInstanceQuery().taskAssignee("zhangsan").list();
        System.out.println("根据执行人获取历史记录：" + JSON.toJSONString(zhangsan));
    }

}
