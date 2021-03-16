package cn.net.yzl.activiti.controller;

import cn.net.yzl.model.dto.ActivitiResult;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    /**
     * 审批通过
     * @return
     */
    public ActivitiResult pass() {
        return null;
    }

    /**
     * 拒绝
     * @return
     */
    public ActivitiResult refuse() {
        return null;
    }
}
