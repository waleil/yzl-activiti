package cn.net.yzl.activiti.enums;

/**
 * 数据源
 */
public enum DataSourceEnum {
    DB1("db1");

    private String value;

    DataSourceEnum(String value){this.value=value;}

    public String getValue() {
        return value;
    }
}
