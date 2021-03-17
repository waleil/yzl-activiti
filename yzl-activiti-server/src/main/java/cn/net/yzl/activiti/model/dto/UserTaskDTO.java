package cn.net.yzl.activiti.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserTaskDTO {
    private String assignee;

    private String name;

    private Date createTime;

    private String executionId;

    private String processInstanceId;

    private String processDefinitionId;

    private String taskDefinitionKey;

    private String id;
}
