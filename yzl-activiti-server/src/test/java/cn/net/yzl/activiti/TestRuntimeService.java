package cn.net.yzl.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 运行时service,处理正在运行状态的流程实例,任务等
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRuntimeService {

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 初始化流程实例
     */
    @Test
    public void initProcessInstance(){
        try {
            //1、获取页面表单填报的内容，请假时间，请假事由，String fromData
            //2、fromData 写入业务表，返回业务表主键ID==businessKey
            //3、把业务数据与Activiti7流程数据关联
            //第一个参数：是指流程定义key
            //第二个参数：业务标识businessKey
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myEvection","bKey001");
            System.out.println("流程实例processInstanceID："+processInstance.getProcessInstanceId());
            System.out.println("流程实例ID："+processInstance.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取流程实例列表
     */
    @Test
    public void getProcessInstances(){
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processInstanceId("c522dc93-82e5-11eb-b6b4-18c04d47ad75").list();
        for(ProcessInstance pi : list){
            System.out.println("--------流程实例------");
            System.out.println("ProcessInstanceId："+pi.getProcessInstanceId());
            System.out.println("ProcessDefinitionId："+pi.getProcessDefinitionId());
            System.out.println("isEnded:"+pi.isEnded());
            System.out.println("isSuspended："+pi.isSuspended());
        }
    }

    /**
     * 暂停与激活流程实例
     */
    @Test
    public void activitieProcessInstance(){
        runtimeService.suspendProcessInstanceById("c522dc93-82e5-11eb-b6b4-18c04d47ad75");
        System.out.println("挂起流程实例");

//        runtimeService.activateProcessInstanceById("44ceb179-82da-11eb-9ab4-18c04d47ad75");
//        System.out.println("激活流程实例");
    }

    /**
     * 删除流程实例
     */
    @Test
    public void delProcessInstance(){
        runtimeService.deleteProcessInstance("7d4a61c2-82da-11eb-b0d2-18c04d47ad75","删除");
        System.out.println("删除流程实例");
    }
}
