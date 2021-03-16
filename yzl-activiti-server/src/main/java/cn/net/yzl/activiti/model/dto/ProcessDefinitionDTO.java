package cn.net.yzl.activiti.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProcessDefinitionDTO {
    private String id;

    private String name;

    private Integer pointNum;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    private Integer status;
}
