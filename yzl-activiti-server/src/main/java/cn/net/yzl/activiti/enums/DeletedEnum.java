package cn.net.yzl.activiti.enums;

/**
 * 数据源
 */
public enum DeletedEnum {
    UNDELETED(0, "未删除"),
    DELETED(-1, "已删除"),;
    private Integer code;
    private String value;

    DeletedEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
