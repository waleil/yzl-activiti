package cn.net.yzl.activiti;

import cn.net.yzl.activiti.config.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
    public void findPersonnelTaskList(){
        String assignee = "rose";//当前任务办理人
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()//与任务相关的Service
                .createTaskQuery()//创建一个任务查询对象
                .taskAssignee(assignee)
                .list();
        if(null != tasks && tasks.size() > 0){
            tasks.forEach(task -> {
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("任务ID:"+task.getId());
                System.out.println("流程实例ID:"+task.getProcessInstanceId());
                System.out.println("#####################################");
            });
        }
    }
}
