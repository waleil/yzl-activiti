ProcessEngine 流程引擎



1. 创建流程。  创建流程 编辑流程 查看流程 删除流程 把bpmn文件保存到对应表即可 
定义bpmn流程文件，定义流程节点。
   
bpmn生成方式（3种）
1. actiBPM插件 最多支持3.0版本 新版idea已不支持 只能做些简单的流程配置
2. activiti explorer
3. BPMN-JS activiti7 推荐
   
事件

![img.png](img.png)
Start Event : 开始事件 流程启动开始节点;
Intermediate Event: 中间事件;
End Event: 结束事件;

活动

![img_1.png](img_1.png)
User Task : 用户任务
Server Task: 服务任务

网关

![img_2.png](img_2.png)
排他网关 (x)
并行网关 (+)
包容网关 (+)
事件网关 (+)

子流程

![img_3.png](img_3.png)


2. 流程部署
```
    /**
     * 流程部署
     */
    @Test
    public void testDeployment(){
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RepositoryServcie
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据中
        Deployment deploy = repositoryService.createDeployment()
                .name("请假申请流程")
                .addClasspathResource("processes/evection.bpmn")
                .addClasspathResource("processes/evection.png")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id="+deploy.getId());
        System.out.println("流程部署名字="+deploy.getName());
    }
```

表操作

act_re_deployment 部署单元信息(act_ge_bytearray父表) 工作流开始表

act_ge_bytearray 通用的流程定义和流程资源

act_re_procdef 已部署的流程定义


3. 加载流程实例  流程挂起 流程激活 查看流程实例 删除流程实例 流程实例相关表不会删除 会有结束时间
```
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
            System.out.println("流程实例ID："+processInstance.getProcessDefinitionId());
        } catch (Exception e) {
            e.printStackTrace();
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
                        withProcessDefinitionKey("myEvection").
                        build());
        log.info("流程实例的内容，{}",processInstance);
    }

    /**
     * 查看流程定义内容
     * Activiti7可以自动部署流程
     */
    @Test
    public void findProcess(){
        securityUtil.logInAs("jack");
        //流程定义的分页对象
        Page<ProcessDefinition> definitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        log.info("可用的流程定义总数：{}",definitionPage.getTotalItems());
        for (ProcessDefinition processDefinition : definitionPage.getContent()) {
            System.out.println("==============================");
            log.info("流程定义内容：{}",processDefinition);
            System.out.println("==============================");
        }
    }
```

表操作
act_hi_actinst 历史的流程实例 select * from act_hi_actinst;

act_hi_identitylink 历史的流程运行过程中用户关系 select * from act_hi_identitylink;

act_hi_procinst 历史的流程实例     select * from act_hi_procinst ;

act_hi_taskinst 历史的任务实例 select * from act_hi_taskinst;

act_ru_identitylink 运行时用户关系信息，存储任务节点与参与者 select * from act_ru_identitylink;

act_ru_task 任务表   select * from act_ru_task ;

act_hi_detail activiti默认属性是audit，只有设置成full时才产生细节数据

5. 执行任务
```
    /**完成任务*/
    @Test
    public void completeTask(){
        String taskID = "c47c1343-8267-11eb-aace-18c04d47ad75";
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService().complete(taskID);
        System.out.println("完成任务："+taskID);
    }
```

6. 实现转交的方式
第一种 配置变量 使用$ 或者# 配置ASSIGNEE_
   
第二种 利用监听

7. 挂起的流程实例不能继续执行任务 可以查看


8. 解决bpmn乱码问题
-Dfile.encoding=UTF-8
   

9. historyService 使用
主要查询历史记录信息 act_hi_actinst、 act_hi_procinst、 act_hi_taskinst、 act_ru_task
用户可以查看当前任务


线性审批（单线路审批）

会签审批（多人审批） 代码量大

条件审批（大于多少天）

Deployment 流程部署 (指定流程key 指定流程名 指定执行人 上传bpmn 上传图片 上传zip(批量) 流程部署增删改查 查询列表和查询xml)
（获取流程名称 部署时间）

ProcessDefinition 流程定义


ProcessInstance 流程实例
Task 任务处理
HistoricTaskInstance 历史任务
