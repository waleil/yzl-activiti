package cn.net.yzl.activiti;

import cn.net.yzl.common.swagger2.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = {"cn.net.yzl.activiti","cn.net.yzl.logger"})
@EnableDiscoveryClient
@EnableSwagger
@EnableAsync
@EnableFeignClients(basePackages = {"cn.net.yzl.activiti.feign"})
@MapperScan("cn.net.yzl.activiti.dao")
public class ActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiApplication.class, args);
    }

}
