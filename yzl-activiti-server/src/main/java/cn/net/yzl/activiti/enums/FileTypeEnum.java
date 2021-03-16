package cn.net.yzl.activiti.enums;

/**
 * 文件类型
 */
public enum FileTypeEnum {
    BPMN(1, "bpmn"),
    PNG(2, "png");



    private Integer code;
    private String value;

    FileTypeEnum(int code, String value) {
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

    private static final FileTypeEnum[] VALUES = FileTypeEnum.values();

    public static FileTypeEnum matcher(String value) {
        // 方式1
        /*for (OrderStatusEnum ele : values()) {
            if (ele.status == value) {
                return ele;
            }
        }*/

        // 方式2 - 推荐
        for (FileTypeEnum ele : VALUES) {
            if (ele.value == value) {
                return ele;
            }
        }
        return FileTypeEnum.BPMN;
    }

}
