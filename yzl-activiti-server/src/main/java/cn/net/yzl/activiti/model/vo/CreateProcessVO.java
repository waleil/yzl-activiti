package cn.net.yzl.activiti.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@ApiModel
@Data
public class CreateProcessVO {

    @ApiModelProperty(value = "流程名称", name = "processName", required = true)
    @NotEmpty(message = "流程名称不能为空")
    private String processName;

    @ApiModelProperty(value = "审批类型", name = "approvalType")
    private String approvalType;

    @ApiModelProperty(value = "事件", name = "event")
    private String event;

    @ApiModelProperty(value = "流程key", name = "processKey", required = true)
    @NotEmpty(message = "流程key不能为空")
    private String processKey;

    @ApiModelProperty(value = "操作人", name = "userName", required = true)
    @NotEmpty(message = "操作人不能为空")
    private String userName;
}
