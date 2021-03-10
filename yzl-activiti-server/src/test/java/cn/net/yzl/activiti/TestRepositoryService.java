package cn.net.yzl.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 流程仓库Service，用于管理流程仓库，例如部署、删除、读取流程资源
 *
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRepositoryService {

    /**
     * 流程部署
     */
    @Test
    public void testDeployment(){
        try {
            // 1、创建ProcessEngine
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            // 2、获取RepositoryServcie
            RepositoryService repositoryService = processEngine.getRepositoryService();
            // 3、使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据中
            //有三种方式加载

            //classpath方式
            Deployment deploy = repositoryService.createDeployment()
                    .name("请假申请流程")
                    .addClasspathResource("processes/evection.bpmn")
                    .addClasspathResource("processes/evection.png")
                    .deploy();

            //获取资源相对路径
//            String bpmnPath = "processes/evection.bpmn";
//            String pngPath = "processes/evection.png";
//            FileInputStream bpmnfileInputStream = new FileInputStream(bpmnPath);
//            FileInputStream pngfileInputStream = new FileInputStream(pngPath);
//            Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署对象相关的Service
//                    .createDeployment()//创建部署对象
//                    .addInputStream("processes.bpmn",bpmnfileInputStream)
//                    .addInputStream("processes.png", pngfileInputStream)
//                    .deploy();//完成部署
            //字符串方式
//            String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><definitions>...</definitions>";
//
//            Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署对象相关的Service
//                    .createDeployment()//创建部署对象
//                    .addString("processes.bpmn",text)
//                    .deploy();

            //批量部署 zip包形式
            //从classpath路径下读取资源文件
//            InputStream in = this.getClass().getClassLoader().getResourceAsStream("processes/evection.zip");
//            ZipInputStream zipInputStream = new ZipInputStream(in);
//            Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署对象相关的Service
//                    .createDeployment()//创建部署对象
//                    .addZipInputStream(zipInputStream)//使用zip方式部署，将helloworld.bpmn和helloworld.png压缩成zip格式的文件
//                    .deploy();//完成部署

            // 4、输出部署信息
            System.out.println("流程部署id="+deploy.getId());
            System.out.println("流程部署名字="+deploy.getName());
        } catch (Exception e) {

        }

    }

}
