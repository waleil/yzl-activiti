package cn.net.yzl.activiti;

import cn.net.yzl.activiti.config.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

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
//    @Test
//    public void findProcess(){
////        流程定义的分页对象
//        Page<ProcessDefinition> definitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
//        log.info("可用的流程定义总数：{}",definitionPage.getTotalItems());
//        for (ProcessDefinition processDefinition : definitionPage.getContent()) {
//            System.out.println("==============================");
//            log.info("流程定义内容：{}",processDefinition);
//            System.out.println("==============================");
//        }
//    }
}
