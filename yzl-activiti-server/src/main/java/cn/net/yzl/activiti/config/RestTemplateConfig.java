package cn.net.yzl.activiti.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        // 添加内容转换器,使用默认的内容转换器
        RestTemplate restTemplate = new RestTemplate();
//        // 设置编码格式为UTF-8
//        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
//        HttpMessageConverter<?> converterTarget = null;
//        for (HttpMessageConverter<?> item : converterList) {
//            if (item.getClass() == StringHttpMessageConverter.class) {
//                converterTarget = item;
//                break;
//            }
//        }
//        if (converterTarget != null) {
//            converterList.remove(converterTarget);
//        }
//        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
//        converterList.add(1, converter);

        return restTemplate;
    }
}
