package cn.net.yzl.activiti.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * act_bpmn_file
 * @author 
 */
@Data
public class ActBpmnFile implements Serializable {
    /**
     * bpmn文件表id
     */
    private Long id;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 审批类型
     */
    private String approvalType;

    /**
     * 事件
     */
    private String event;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件类型
     */
    private Byte fileType;

    /**
     * 文件状态
     */
    private Byte fileStatus;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creater;

    private static final long serialVersionUID = 1L;
}