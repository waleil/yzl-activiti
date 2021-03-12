package cn.net.yzl.activiti;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
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
 * activiti的历史记录级别分为以上四种：none, activity, audit, full
 *
 * none: 不记录历史流程，性能高，流程结束后不可读取
 * activiti: 归档流程实例和活动实例，流程变量不同步
 * audit: 默认值，在activiti基础上同步变量值，保存表单属性
 * full: 性能较差，记录所有实例和变量细节变化
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
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processInstanceId("0144c714-82dd-11eb-85df-18c04d47ad75").list();
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

        //根据processInstanceId 可以查询当前流程实例属性信息 历史综合信息
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId("30316d0a-8242-11eb-9771-18c04d47ad75").list();
        System.out.println("根据流程id获取历史记录：" + JSON.toJSONString(list));
    }

    //根据用户名查询历史记录
    @Test
    public void HistoricTaskInstanceByUser(){
        // 获取流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = engine.getHistoryService();
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .taskAssignee("zhangsan")
                .list();
        for(HistoricTaskInstance hi : list){
            System.out.println("Id："+ hi.getId());
            System.out.println("ProcessInstanceId："+ hi.getProcessInstanceId());
            System.out.println("Name："+ hi.getName());
            System.out.println("=======================================");
        }
    }

    /**
     * 主要获取act_hi_taskinst表中历史任务属性信息
     */
    @Test
    public void testRepositoryService2() {
        // 获取流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = engine.getHistoryService();

        //根据processInstanceId 可以查询当前流程实例属性信息 历史综合信息
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().list();
        System.out.println("根据流程id获取历史记录：" + JSON.toJSONString(list));
    }

    /**
     * 查询历史流程实例
     */
    @Test
    public void queryHistoricInstance() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        List<HistoricProcessInstance> list = engine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .orderByProcessInstanceStartTime().asc()//排序
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricProcessInstance hpi : list) {
                System.out.println("流程定义ID：" + hpi.getProcessDefinitionId());
                System.out.println("流程实例ID：" + hpi.getId());
                System.out.println("开始时间：" + hpi.getStartTime());
                System.out.println("结束时间：" + hpi.getEndTime());
                System.out.println("流程持续时间：" + hpi.getDurationInMillis());
                System.out.println("=======================================");
            }
        }
    }

    /**
     * 流程执行明细
     */
    @Test
    public void queryHistoricActivitiInstance() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        List<HistoricActivityInstance> list = engine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processInstanceId("30316d0a-8242-11eb-9771-18c04d47ad75")
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricActivityInstance hai : list) {
                System.out.println(hai.getId());
                System.out.println("步骤ID：" + hai.getActivityId());
                System.out.println("步骤名称：" + hai.getActivityName());
                System.out.println("执行人：" + hai.getAssignee());
                System.out.println("====================================");
            }
        }
    }

    /**
     * 流程的执行经历的多少任务
     */
    @Test
    public void queryHistoricTask() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        List<HistoricTaskInstance> list = engine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .processInstanceId("0144c714-82dd-11eb-85df-18c04d47ad75")
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance hti : list) {
                System.out.println("taskId:" + hti.getId()+"，");
                System.out.println("name:" + hti.getName()+"，");
                System.out.println("pdId:" + hti.getProcessDefinitionId()+"，");
                System.out.println("assignee:" + hti.getAssignee()+"，");
                System.out.println("====================================");
            }
        }
    }

}
