package cn.net.yzl.activiti;

import cn.net.yzl.common.swagger2.EnableSwagger;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"cn.net.yzl.activiti","cn.net.yzl.logger"})
@EnableDiscoveryClient
@EnableSwagger
@EnableAsync
@EnableRabbit
@EnableFeignClients(basePackages = {"cn.net.yzl.activiti.feign"})
public class ActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiApplication.class, args);
    }

}
