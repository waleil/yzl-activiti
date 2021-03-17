package cn.net.yzl.activiti.service;

import cn.net.yzl.common.entity.ComResponse;

public interface ITaskService {
    /**
     * 获取当前登录人任务
     * @param userName
     * @return
     */
    ComResponse getTask(String userName);

    /**
     * 通过
     * @param taskId
     * @return
     */
    ComResponse pass(String taskId);
}
