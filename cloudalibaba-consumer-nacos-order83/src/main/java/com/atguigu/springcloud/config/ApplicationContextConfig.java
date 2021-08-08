package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class ApplicationContextConfig
{
    @Bean
    @LoadBalanced // 使用RestTemplate + Rabbit实现负载均衡需要添加此注解 LoadBalanced是一个接口，Ribbon实现了这个接口
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }
}
 
 
