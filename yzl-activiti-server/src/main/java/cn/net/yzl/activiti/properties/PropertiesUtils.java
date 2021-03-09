package cn.net.yzl.activiti.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * +  钉钉 api 配置
 */
@Data
@ConfigurationProperties(prefix = "appid")
@Component
public class PropertiesUtils {


    public List<String> rAppidList;

    public List<String> rwAppidList;


}
