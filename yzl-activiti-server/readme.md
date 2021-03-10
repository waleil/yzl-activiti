ProcessEngine 流程引擎



1.创建流程。
定义bpmn流程文件，定义流程节点。

2.加载流程
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

涉及表操作

act_re_deployment 部署单元信息(act_ge_bytearray父表)

act_ge_bytearray 通用的流程定义和流程资源

act_re_procdef 已部署的流程定义


3. 查询流程内容
```
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
4.启动流程
```
    /**
     * 启动流程
     */
    @Test
    public void startProcess(){
        //设置登录用户
        securityUtil.logInAs("system");
        ProcessInstance processInstance = processRuntime.
                start(ProcessPayloadBuilder.
                        start().
                        withProcessDefinitionKey("mydemo").
                        build());
        log.info("流程实例的内容，{}",processInstance);
    }
```
5.执行任务
```
    /**
     * 执行任务
     */
    @Test
    public void doTask(){
        //设置登录用户
        securityUtil.logInAs("jerry");
        //查询任务
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 10));
        if(taskPage != null && taskPage.getTotalItems()>0){
            for (Task task : taskPage.getContent()) {
                //        拾取任务
                taskRuntime.claim(TaskPayloadBuilder.
                        claim().
                        withTaskId(task.getId()).
                        build());
                log.info("任务内容,{}",task);
                //        完成任务
                taskRuntime.complete(TaskPayloadBuilder.
                        complete().
                        withTaskId(task.getId()).
                        build());
            }
        }
    }
```