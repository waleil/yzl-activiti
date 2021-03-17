package cn.net.yzl.activiti.service;

import cn.net.yzl.common.entity.ComResponse;

public interface IHistoryService {
    /**
     * 获取流程实例执行明细
     * @param processInstanceId
     * @return
     */
    ComResponse historyDetail(String processInstanceId);
}
