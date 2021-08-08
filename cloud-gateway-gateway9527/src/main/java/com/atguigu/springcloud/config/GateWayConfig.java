package com.atguigu.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateWayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        // 配置了一个id为path_route_atguigu的路由规则
        routes.route("path_route_atguigu",
                // 访问本地的localhost/lady将会转发到http://news.baidu.com/lady
                r -> r.path("/lady")
                            .uri("http://news.baidu.com/lady")).build();

        return routes.build();
    }

}
