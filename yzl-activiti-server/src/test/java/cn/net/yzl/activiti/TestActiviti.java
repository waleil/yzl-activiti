package cn.net.yzl.activiti;

import cn.net.yzl.activiti.config.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestActiviti {

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
    public void findProcessDefinitions(){
        securityUtil.logInAs("system");
        //流程定义的分页对象
        Page<ProcessDefinition> definitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        log.info("可用的流程定义总数：{}",definitionPage.getTotalItems());
        for (ProcessDefinition processDefinition : definitionPage.getContent()) {
            System.out.println("==============================");
            log.info("流程定义内容：{}",processDefinition);
            System.out.println("==============================");
        }
    }

    /**
     * 查看流程实例内容
     * Activiti7可以自动部署流程
     */
    @Test
    public void findProcessInstances(){
        securityUtil.logInAs("system");
        //流程定义的分页对象
        Page<ProcessInstance> processInstancePage = processRuntime.processInstances(Pageable.of(0, 10));
        log.info("可用的流程实例总数：{}",processInstancePage.getTotalItems());
        for (ProcessInstance processInstance : processInstancePage.getContent()) {
            System.out.println("==============================");
            log.info("流程实例内容：{}",processInstance);
            System.out.println("==============================");
        }
    }

    /**
     * 启动流程
     */
    @Test
    public void startProcess(){
        //设置登录用户 根据流程key获取当前流程实例 构建
        securityUtil.logInAs("system");
        ProcessInstance processInstance = processRuntime.
                start(ProcessPayloadBuilder.
                        start().
                        withProcessDefinitionKey("exclusive").withBusinessKey("bKey001").
                        build());
        log.info("流程实例的内容，{}",processInstance);
    }

    /**完成任务*/
    @Test
    public void completeTask(){
        String taskID = "5eb75925-8589-11eb-b8c3-005056c00001";
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService().complete(taskID, null, false);
        System.out.println("完成任务："+taskID);
    }

    /**
     * 执行任务
     */
    @Test
    public void doTask(){
        //设置登录用户
        securityUtil.logInAs("jack");
        //查询任务
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 10));
        if(taskPage != null && taskPage.getTotalItems()>0){
            for (Task task : taskPage.getContent()) {
                //拾取任务
                taskRuntime.claim(TaskPayloadBuilder.
                        claim().
                        withTaskId(task.getId()).
                        build());
                log.info("任务内容,{}",task);
                //完成任务
                taskRuntime.complete(TaskPayloadBuilder.
                        complete().
                        withTaskId(task.getId()).
                        build());
            }
        }
    }


}
