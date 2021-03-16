package cn.net.yzl.activiti;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 流程仓库Service，用于管理流程仓库，例如部署、删除、读取流程资源
 * 可以提供接口 保存流程 查看流程 删除流程 编辑流程（在审批中得流程如何处理？）
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRepositoryService {

    @Autowired
    private RepositoryService repositoryService;
    /**
     * 流程定义
     */
    @Test
    public void testDeployment(){
        try {
            // 3、使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据中
            //有三种方式加载

            //classpath方式
            Deployment deploy = repositoryService.createDeployment()
                    .name("请假申请流程")
                    .addClasspathResource("processes/evection-exclusive.bpmn")
                    .key("CRM0001")
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
            e.printStackTrace();
        }
    }

    /**
     * processDefinition he processInstance
     * ProcessDefinition 流程定义
     * processInstance 流程实例
     *
     */
    //查询流程
    @Test
    public void getDefinitions(){

        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().deploymentId("bc123061-853d-11eb-9ee3-005056c00001")
                .list();
        for(ProcessDefinition pd : processDefinitionList){
            System.out.println("------流程--------");
            System.out.println("Name："+pd.getName());
            System.out.println("Key："+pd.getKey());
            System.out.println("ResourceName："+pd.getResourceName());
            System.out.println("DeploymentId："+pd.getDeploymentId());
            System.out.println("Version："+pd.getVersion());
            System.out.println("diagramResourceName："+pd.getDiagramResourceName());
            System.out.println("category："+pd.getCategory());
        }
        System.out.println("============================================");

        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().deploymentId("bc123061-853d-11eb-9ee3-005056c00001").list();
        for (Deployment deployment: deploymentList) {
            System.out.println(deployment.getName());
            System.out.println(deployment.getId());
            System.out.println(deployment.getKey());
            System.out.println(deployment.getDeploymentTime());
        }
    }

    /**
     * 删除流程 参数 id
     */
    @Test
    public void deleteDeployment() {
        repositoryService.deleteDeployment("c85afcdc-853c-11eb-bac4-005056c00001");
    }
}
