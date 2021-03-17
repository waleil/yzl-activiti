package cn.net.yzl.activiti.service;

import cn.net.yzl.activiti.utils.AjaxResponse;
import cn.net.yzl.model.dto.ActivitiResult;


public interface IActTaskService {
    /**
     * 审批通过
     */
    ActivitiResult pass();


    /**
     * 拒绝
     */
    ActivitiResult refuse();

    /**
     * 查询代办任务
     * @param userName
     */
    AjaxResponse getTasks(String userName);


    /**
     * 完成代办任务
     * @param userName
     * @param taskID
     */
    AjaxResponse completeTask(String userName,String taskID);

}
