package cn.net.yzl.model.dto;

import cn.net.yzl.common.util.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author limisheng
 * @version 1.0
 * @title: ActivitiBaseDto
 * @date: 2021/03/09 10:00
 */
@ApiModel(value = "ActivitiBaseDto", description = "activiti返回数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActivitiResult<T> implements Serializable {
    private static final long serialVersionUID = 516582896453301962L;
    @ApiModelProperty(value = "返回code", name = "code", required = false)
    private String code;
    @ApiModelProperty(value = "返回msg", name = "msg", required = false)
    private String msg;
    @ApiModelProperty(value = "返回数据", name = "result", required = false)
    private String result;
    private Boolean success;
    private T data;

    public static ActivitiResult getInstance(String res) {
        ActivitiResult result = JsonUtil.getObjectFromJSONString(res, ActivitiResult.class);
        if ("10000".equals(result.getCode())) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }
}
