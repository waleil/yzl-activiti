Activiti数据库表结构
1.1      数据库表名说明
Activiti工作流总共包含23张数据表，所有的表名默认以“ACT_”开头。

并且表名的第二部分用两个字母表明表的用例，而这个用例也基本上跟Service API匹配。

u  ACT_GE_* : “GE”代表“General”（通用），用在各种情况下；

u  ACT_HI_* : “HI”代表“History”（历史），这些表中保存的都是历史数据，比如执行过的流程实例、变量、任务，等等。Activit默认提供了4种历史级别：

Ø  none: 不保存任何历史记录，可以提高系统性能；

Ø  activity：保存所有的流程实例、任务、活动信息；

Ø  audit：也是Activiti的默认级别，保存所有的流程实例、任务、活动、表单属性；

Ø  full：最完整的历史记录，除了包含audit级别的信息之外还能保存详细，例如：流程变量。

对于几种级别根据对功能的要求选择，如果需要日后跟踪详细可以开启full。



u  ACT_ID_* : “ID”代表“Identity”（身份），这些表中保存的都是身份信息，如用户和组以及两者之间的关系。如果Activiti被集成在某一系统当中的话，这些表可以不用，可以直接使用现有系统中的用户或组信息；

u  ACT_RE_* : “RE”代表“Repository”（仓库），这些表中保存一些‘静态’信息，如流程定义和流程资源（如图片、规则等）；

u  ACT_RU_* : “RU”代表“Runtime”（运行时），这些表中保存一些流程实例、用户任务、变量等的运行时数据。Activiti只保存流程实例在执行过程中的运行时数据，并且当流程结束后会立即移除这些数据，这是为了保证运行时表尽量的小并运行的足够快；


![img_4.png](img_4.png)


2  Activiti中主要对象的关系
本节主要介绍在工作流中出现的几个对象及其之间的关系，以及在Activiti中各个对象是如何关联的。

在开始之前先看看下图，对整个对象结构有个了解，再结合实例详细介绍理解。

图1.Activiti中几个对象之间的关系

我们模拟一个请假的流程进行分析介绍，该流程主要包含以下几个步骤：

u  员工申请请假

u  部门领导审批

u  人事审批

u  员工销假



ProcessInstance对象

员工开始申请请假流程，通过runtimeService.startProcessInstance()方法启动，引擎会创建一个流程实例（ProcessInstance）。

简单来说流程实例就是根据一次（一条）业务数据用流程驱动的入口，两者之间是一对一的关系。流程引擎会创建一条数据到ACT_RU_EXECUTION表，同时也会根据history的级别决定是否查询相同的历史数据到ACT_HI_PROCINST表。

启动完流程之后业务和流程已经建立了关联关系，第一步结束。

启动流程和业务关联区别：

u 对于自定义表单来说启动的时候会传入businessKey作为业务和流程的关联属性

u 对于动态表单来说不需要使用businessKey关联，因为所有的数据都保存在引擎的表中

u 对于外部表单来说businessKey是可选的，但是一般不会为空，和自定义表单类似



Execution对象

对于初学者来说，最难理解的地方就是ProcessInstance与Execution之间的关系，要分两种情况说明。Execution的含义就是一个流程实例（ProcessInstance）具体要执行的过程对象。

不过在说明之前先声明两者的对象映射关系：

ProcessInstance（1）→ Execution(N)，（其中N>=1）。

1)  值相等的情况：

除了在流程中启动的子流程之外，流程启动之后在表ACT_RU_EXECUTION中的字段ID_和PROC_INST_ID_字段值是相同的。

图2.ID_和PROC_INST_ID_相等

2)  值不相等的情况：

不相等的情况目前只会出现在子流程中（包含：嵌套、引入），例如一个购物流程中除了下单、出库节点之外可能还有一个付款子流程，在实际企业应用中付款流程通常是作为公用的，所以使用子流程作为主流程（购物流程）的一部分。

当任务到达子流程时引擎会自动创建一个付款流程，但是这个流程有一个特殊的地方，在数据库可以直观体现，如下图。

图3.ID_和PROC_INST_ID_不相等

上图中有两条数据，第二条数据（嵌入的子流程）的PARENT_ID_等于第一条数据的ID_和PROC_INST_ID_，并且两条数据的PROC_INST_ID_相同。

上图中还有一点特殊的地方，字段IS_ACTIVE_的值分别是0和1，说明正在执行子流程主流程挂起。



Task对象

前面说了ProcessInstance和业务是一对一关联的，和业务数据最亲密；而Task则和用户最亲密的（UserTask），用户每天的待办事项就是一个个的Task对象。

从图1中看得出Execution和Task是一对一关系，Task可以是任何类型的Task实现，可以是用户任务（UserTask）、Java服务（JavaServiceTask）等，在实际流程运行中只不过面向对象不同，用户任务(UserTask)需要有人为参与完成（complete），Java服务需要由系统自动执行（execution）。

图4. 表ACT_RU_TASK

Task是在流程定义中看到的最大单位，每当一个Task完成的时候引擎会把当前的任务移动到历史中，然后插入下一个任务插入到表ACT_RU_TASK中。结合请假流程来说就是让用户点击“完成”按钮提交当前任务是的动作，引擎自动根据任务的顺序流或者排他分支判断走向。



HistoryActivity（历史活动）

图5. 表ACT_HI_ACTINST



Activity包含了流程中所有的活动数据，例如开始事件（图5表中的第1条数据）、各种分支（排他分支、并行分支等，图5表中的第2条数据）、以及刚刚提到的Task执行记录（如图5表中的第3、4条数据）。

有些人认为Activity和Task是多对一关系，其实不是，从上图中可以看出来根本没有Task相关的字段。

结合请假流程来说，如Task中提到的当完成流程的时候所有下一步要执行的任务（包括各种分支）都会创建一个Activity记录到数据库中。例如领导审核节点点击“同意”按钮就会流转到人事审批节点，如果“驳回”那就流转到调整请假内容节点，每一次操作的Task背后实际记录更详细的活动（Activity）


Activiti使用流程
第一步： 引入相应jar包并初始化数据库
既然activiti是一个框架，那么我们肯定是需要引入对应的jar包坐标的。
第二步： 通过工具绘画流程图
使用 activiti 流程建模工具(activity-designer)定义业务流程(.bpmn 文件) 。.bpmn 文件就是业务流程定义文件，通过 xml 定义业务流程。
第三步：流程定义部署
向 activiti 部署业务流程定义（.bpmn 文件），使用 activiti 提供的 api 向 activiti 中部署.bpmn 文件
第四步： 启动一个流程实例（ProcessInstance）
启动一个流程实例表示开始一次业务流程的运行，比如员工请假流程部署完成，如果张三要请假就可以启动一个流程实例，如果李四要请假也启动一个流程实例，两个流程的执行互相不影响，就好比定义一个 java 类，实例化两个对象一样，部署的流程就好比 java 类，启动一个流程实例就好比 new 一个 java 对象
第五步： 用户查询待办任务(Task)
因为现在系统的业务流程已经交给 activiti 管理，通过 activiti 就可以查询当前流程执行到哪了，当前用户需要办理什么任务了，这些 activiti帮我们管理了。实际上我们学习activiti也只是学习它的API怎么使用，因为很多功能activiti都已经封装好了，我们会调用就行了~
第六步： 用户办理任务
用户查询待办任务后，就可以办理某个任务，如果这个任务办理完成还需要其它用户办理，比如采购单创建后由部门经理审核，这个过程也是由 activiti 帮我们完成了，不需要我们在代码中硬编码指定下一个任务办理人了
第七步： 流程结束
当任务办理完成没有下一个任务/结点了，这个流程实例就完成了。


1.Activiti的架构说明

ProcessEngineConfiguration类,主要作用是加载activiti.cfg.xml配置文件

ProcessEngine类 作用是帮助我们可以快速得到各个Service接口，并且可以生成activiti的工作环境 25张表生成

Service接口          作用：可以快速实现数据25张表的操作。

RepositoryService

RuntimeService

TaskService

HistoryService



2.用BPMN的ActivitiDesigner插件绘制流程定义图

3.部署流程定义

方式一：单个文件（bpmn文件，png文件）


/**
* 流程定义的部署
* 影响的activiti表有哪些
* act_re_deployment 部署信息
* act_re_procdef    流程定义的一些信息
* act_ge_bytearray  流程定义的bpmn文件以及png文件
  */
  public class ActivitiDeployment {
  // 流程定义部署
  public static void main(String[] args){
  //1.创建ProcessEngine对象
  ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

       //2.得到RepositoryService实例
       RepositoryService repositoryService = processEngine.getRepositoryService();

       //3.进行部署
       Deployment deployment = repositoryService.createDeployment()//创建Deployment对象
               .addClasspathResource("diagram/holiday.bpmn")//添加bpmn文件
               .addClasspathResource("diagram/holiday.png")//添加png文件
               .name("请假申请单流程")
               .deploy();//部署

       //4.输出部署的一些信息
       System.out.println(deployment.getName());
       System.out.println(deployment.getId());
  }
  }
 


方式二：先将bpmn文件和png文件压缩成zip文件。但是activiti最终也是以单个文件形式保存，说明activiti进行了解压工作。


/**
* Zip文件部署流程
* 影响的activiti表有哪些
*  act_re_deployment 部署信息
*  act_re_procdef    流程定义的一些信息
*  act_ge_bytearray  流程定义的bpmn文件以及png文件
   */
   public class ActivitiZipDeployment {
   // 流程定义部署
   public static void main(String[] args){
   //1.创建ProcessEngine对象
   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

       //2.得到RepositoryService实例
       RepositoryService repositoryService = processEngine.getRepositoryService();
       
       //3.转换出ZipInputStream流对象
       InputStream is = ActivitiZipDeployment.class.getClass().getClassLoader().getResourceAsStream("holidayBPMN.zip");

       //将InputStream流转化为ZipInputStream
       ZipInputStream zipInputStream = new ZipInputStream(is);

       //3.进行部署
       Deployment deployment = repositoryService.createDeployment()//创建Deployment对象
               .addZipInputStream(zipInputStream)
               .name("请假申请单流程")
               .deploy();//部署

       //4.输出部署的一些信息
       System.out.println(deployment.getName());
       System.out.println(deployment.getId());
   }
   }
 
   4.启动流程实例：


/**
* 启动流程实例:
*      前提是先已经完成流程定义的部署工作
*
*      背后影响的表：
*      act_hi_actinst      已完成的活动信息
*      act_hi_identitylink   参与者信息
*      act_hi_procinst     流程实例
*      act_hi_taskinst     任务实例
*      act_ru_execution    执行表
*      act_ru_identitylink   参与者信息
*      act_ru_task   任务表
*/
public class ActivitiStartInstance {
public static void main(String[] args) {
//1.得到ProcessEngine对象
ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//2.得到RunService对象
RuntimeService runtimeService = processEngine.getRuntimeService();
//3.创建流程实例(关键步骤)即 启动流程实例
//需要知道流程定义的Key：holiday（找key的方法  1：bpmn文件中的id，它对应的值就是key
// 2：直接看数据库中流程定义表act_re_procdet的key值）
ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday");
//4.输出实例的相关信息
System.out.println("流程部署ID="+processInstance.getDeploymentId());//null
System.out.println("流程定义ID="+processInstance.getProcessDefinitionId());//holiday:1:4
System.out.println("流程实例ID="+processInstance.getId());//2501
System.out.println("流程活动ID="+processInstance.getActivityId());//获取当前具体执行的某一个节点的ID(null)

    }
}

5.查看任务

TaskService　　　　taskService.createTaskQuery()


/**
* 查询当前用户的任务列表
  */
  public class ActivitiTaskQuery {
  //lisi完成自己任务列表的查询
  public static void main(String[] args) {
  //1.得到ProcessEngine对象
  ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

       //2.得到TaskService对象
       TaskService taskService = processEngine.getTaskService();
       //3.根据流程定义的key以及负责人assignee来实现当前用户的任务列表查询
       List<Task> taskList = taskService.createTaskQuery()
               .processDefinitionKey("holiday")
               .taskAssignee("lisi")
               .list();//这里还有一个查询唯一结果的方法：singleResult();、还有分页查询listPage(index,limit);
       //4.任务列表展示
       for (Task task : taskList) {
           //查的act_hi_procinst表的id
           System.out.println("流程实例ID="+task.getProcessInstanceId());
           //查的act_hi_taskinst表的id
           System.out.println("任务ID="+task.getId());
           //查的act_hi_taskinst表的Assignee_
           System.out.println("任务负责人名称="+task.getAssignee());
           //查的act_hi_taskinst表的NAME_
           System.out.println("任务名称="+task.getName());
       }
  }
  }
  复制代码
  6.完成任务

TaskService　　　　taskService.complete(task.getId());//参数为任务ID


/**
* 处理当前用户的任务列表
*  背后操作到的表：
*           act_hi_actinst
*           act_hi_identitylink
*           act_hi_taskinst
*           act_ru_execution
*           act_ru_identitylink
*           act_ru_task //只放当前要执行的任务
*/
public class ActivitiCompleteTask {
/**
* 李四完成自己的任务
* @param args
*/
public static void main(String[] args) {
//1.得到ProcessEngine对象
ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.处理任务,结合当前用户任务列表的查询操作的话，可以知道任务ID=5002(实际操作中应该与查询写在一起)
        taskService.complete("5002");
    }
}

Activiti使用流程
第一步： 引入相应jar包并初始化数据库
既然activiti是一个框架，那么我们肯定是需要引入对应的jar包坐标的，具体参考代码中的。
第二步： 通过工具绘画流程图
使用 activiti 流程建模工具(activity-designer)定义业务流程(.bpmn 文件) 。.bpmn 文件就是业务流程定义文件，通过 xml 定义业务流程。
第三步：流程定义部署
向 activiti 部署业务流程定义（.bpmn 文件），使用 activiti 提供的 api 向 activiti 中部署.bpmn 文件
第四步： 启动一个流程实例（ProcessInstance）
启动一个流程实例表示开始一次业务流程的运行，比如员工请假流程部署完成，如果张三要请假就可以启动一个流程实例，如果李四要请假也启动一个流程实例，两个流程的执行互相不影响，就好比定义一个 java 类，实例化两个对象一样，部署的流程就好比 java 类，启动一个流程实例就好比 new 一个 java 对象
第五步： 用户查询待办任务(Task)
因为现在系统的业务流程已经交给 activiti 管理，通过 activiti 就可以查询当前流程执行到哪了，当前用户需要办理什么任务了，这些 activiti帮我们管理了。实际上我们学习activiti也只是学习它的API怎么使用，因为很多功能activiti都已经封装好了，我们会调用就行了~
第六步： 用户办理任务
用户查询待办任务后，就可以办理某个任务，如果这个任务办理完成还需要其它用户办理，比如采购单创建后由部门经理审核，这个过程也是由 activiti 帮我们完成了，不需要我们在代码中硬编码指定下一个任务办理人了
第七步： 流程结束
当任务办理完成没有下一个任务/结点了，这个流程实例就完成了。



环境准备：
activiti 7
jdk1.8
开发IDE：IDEA2019.1.1
mysql：5.7
具体jar包依赖，日志配置 参考代码中的



任务查询
流程启动后，任务的负责人就可以查询自己当前需要处理的任务，查询出来的任务都是该用户的待办任务。

二、个人任务
2.1、分配任务负责人
2.1.1、固定分配
2.1.2、表达式分配
并在 properties 视图中，填写 Assignee 项为任务负责人。
由于固定分配方式，任务只管一步一步执行任务，执行到每一个任务将按照 bpmn 的配置去分配任 务负责人。
2.1.2.1、UEL 表达式
Activiti 使用 UEL 表达式， UEL 是 java EE6 规范的一部分， UEL（Unified Expression Language）即 统一表达式语言，
activiti 支持两个 UEL 表达式： UEL-value 和 UEL-method。

设置任务候选人
下一级任务的审批人


