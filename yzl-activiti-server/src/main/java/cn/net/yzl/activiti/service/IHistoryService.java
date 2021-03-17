package cn.net.yzl.activiti.service;

import cn.net.yzl.common.entity.ComResponse;

public interface IHistoryService {
    /**
     * 获取流程实例执行明细
     * @param processInstanceId
     * @return
     */
    ComResponse historyDetail(String processInstanceId);

    /**
     * 我发起的流程
     * @param userName
     * @return
     */
    ComResponse myCreated(String userName);

    /**
     * 我审批的流程
     * @param userName
     * @return
     */
    ComResponse myApproved(String userName);
}
