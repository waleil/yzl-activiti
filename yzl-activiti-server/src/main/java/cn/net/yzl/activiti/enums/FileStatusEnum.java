package cn.net.yzl.activiti.enums;

/**
 * 文件状态
 */
public enum FileStatusEnum {
    NORMAL(1, "正常"),
    DELETED(2, "删除");


    private Integer code;
    private String value;


    FileStatusEnum(int code, String value) {
        this.code = code;
        this.value=value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
