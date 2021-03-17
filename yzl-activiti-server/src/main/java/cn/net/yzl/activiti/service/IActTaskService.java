package cn.net.yzl.activiti.service;

import cn.net.yzl.activiti.model.vo.RejectedVO;
import cn.net.yzl.activiti.utils.AjaxResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.dto.ActivitiResult;


public interface IActTaskService {

    /**
     * 审批拒绝
     * @param userName
     * @param taskId
     * @return
     */
    ComResponse pass(String userName, String taskId);

    /**
     * 查询代办任务
     * @param userName
     */
    AjaxResponse getTasks(String userName);

    /**
     * 审批拒绝
     * @param rejectedVO
     * @return
     */
    ComResponse rejected(RejectedVO rejectedVO);
}
