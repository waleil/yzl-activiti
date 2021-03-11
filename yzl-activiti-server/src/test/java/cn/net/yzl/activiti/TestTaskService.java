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
     * 查看任务详情
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


    //根据候选人查询组任务
    @Test
    public void findGroupTaskList() {
        // 流程定义key
        String processDefinitionKey = "evection3";
        // 任务候选人
        String candidateUser = "lisi";
        // 获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        //查询组任务
        List<Task> list = taskService.createTaskQuery()
        .processDefinitionKey(processDefinitionKey)
        .taskCandidateUser(candidateUser)//根据候选人查询
        .list();
        for (Task task : list) {
        System.out.println("----------------------------");
        System.out.println("流程实例id：" + task.getProcessInstanceId());
        System.out.println("任务id：" + task.getId());
        System.out.println("任务负责人：" + task.getAssignee());
        System.out.println("任务名称：" + task.getName());
    }

  }

    //拾取组任务 候选人员拾取组任务后该任务变为自己的个人任务
    @Test
    public void claimTask(){
        // 获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //要拾取的任务id
        String taskId = "6302";
        //任务候选人id
        String userId = "lisi";
        //拾取任务
        //即使该用户不是候选人也能拾取(建议拾取时校验是否有资格)  
        //校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery()
        .taskId(taskId)
        .taskCandidateUser(userId)//根据候选人查询
        .singleResult();
        if(task!=null){
        //拾取任务
        taskService.claim(taskId, userId);
        System.out.println("任务拾取成功");
        }
}

    //查询个人待办任务 查询方式同个人任务查询
    @Test
    public void findPersonalTaskList() {
        // 流程定义key
        String processDefinitionKey = "evection1";
        // 任务负责人
        String assignee = "zhangsan";
        // 获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey)
        .taskAssignee(assignee)
        .list();
        for (Task task : list) {
        System.out.println("----------------------------");
        System.out.println("流程实例id：" + task.getProcessInstanceId());
        System.out.println("任务id：" + task.getId());
        System.out.println("任务负责人：" + task.getAssignee());
        System.out.println("任务名称：" + task.getName());
        }
    }

    //办理个人任务 同个人任务办理
    /*完成任务*/
    @Test
    public void completeTask(){
        // 任务ID
        String taskId = "12304";
        // 获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
        .complete(taskId);
        System.out.println("完成任务："+taskId);
    }


    /*
     *归还组任务，由个人任务变为组任务，还可以进行任务交接
     */
    @Test
    public void setAssigneeToGroupTask() {
        // 获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 查询任务使用TaskService
        TaskService taskService = processEngine.getTaskService();
        // 当前待办任务
        String taskId = "6004";
        // 任务负责人
        String userId = "zhangsan2";
        // 校验userId是否是taskId的负责人，如果是负责人才可以归还组任务
        Task task = taskService
        .createTaskQuery()
        .taskId(taskId)
        .taskAssignee(userId)
        .singleResult();
        if (task != null) {
        // 如果设置为null，归还组任务,该 任务没有负责人
        taskService.setAssignee(taskId, null);
        }
    }


    //任务交接,任务负责人将任务交给其它候选人办理该任务
    @Test
    public void setAssigneeToCandidateUser() {
        // 获取processEngine
                ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 查询任务使用TaskService
        TaskService taskService = processEngine.getTaskService();
        // 当前待办任务
        String taskId = "6004";
        // 任务负责人
        String userId = "zhangsan2";
        // 将此任务交给其它候选人办理该 任务
        String candidateuser = "zhangsan";
        // 校验userId是否是taskId的负责人，如果是负责人才可以归还组任务
        Task task = taskService
        .createTaskQuery()
        .taskId(taskId)
        .taskAssignee(userId)
        .singleResult();
        if (task != null) {
        taskService.setAssignee(taskId, candidateuser);
        }
    }

}



