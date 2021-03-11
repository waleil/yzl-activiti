package cn.net.yzl.activiti;

import cn.net.yzl.activiti.config.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 任务service,管理,查询任务，例如签收，办理,指派任务。
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTaskService {
    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 查看流程定义内容
     * Activiti7可以自动部署流程
     */
    @Test
    public void findPersonnelTaskList() {
        String assignee = "zhangsan";//当前任务办理人
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()//与任务相关的Service
                .createTaskQuery()//创建一个任务查询对象
                .taskAssignee(assignee)
                .list();
        if (null != tasks && tasks.size() > 0) {
            tasks.forEach(task -> {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务ID:" + task.getId());
                System.out.println("流程实例ID:" + task.getProcessInstanceId());
                System.out.println("#####################################");
            });
        }

    }

        /**
         * 测试完成个人任务
         */
        @Test
        public void completTask(){
            //获取引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            //获取操作任务的服务 TaskService
            TaskService taskService = processEngine.getTaskService();
            //完成任务，参数 流程实例id 完成的任务
            Task task = taskService.createTaskQuery().processDefinitionId("15001").taskAssignee("rose").singleResult();
            log.info("流程实例id：{}",task.getProcessInstanceId());
            log.info("任务id：{}",task.getId());
            log.info("任务负责人：{}",task.getAssignee());
            log.info("任务名称:{}",task.getName());
            taskService.complete(task.getId());

        }
        @Test
        public void findProcessInstance(){
        //获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(); //  获取TaskService
        TaskService taskService = processEngine.getTaskService(); //  获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService(); //  查询流程定义的对象
        Task task = taskService.createTaskQuery()
        .processDefinitionKey("myEvection1")
        .taskAssignee("张三")
        .singleResult();
        //使用task对象获取实例id
        String processInstanceId = task.getProcessInstanceId();
        //使用实例id，获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
        .processInstanceId(processInstanceId)
        .singleResult();
        //使用processInstance，得到 businessKey
        String businessKey = processInstance.getBusinessKey();
        System.out.println("businessKey=="+businessKey);

  }

    }



