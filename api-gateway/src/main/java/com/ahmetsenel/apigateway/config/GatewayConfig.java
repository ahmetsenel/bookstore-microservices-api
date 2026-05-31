package com.ahmetsenel.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("user-service", r -> r
                        .path("/api/auth/**",
                                "/api/users/**",
                                "/api/admin/users/**")
                        .uri("lb://USER-SERVICE"))

                .route("book-service", r -> r
                        .path("/api/books/**",
                                "/api/admin/books/**")
                        .uri("lb://BOOK-SERVICE"))

                .route("order-service", r -> r
                        .path("/api/orders/**",
                                "/api/admin/orders/**")
                        .uri("lb://ORDER-SERVICE"))


                .route("user-service-docs", r -> r
                        .path("/user-service/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://USER-SERVICE"))

                .route("book-service-docs", r -> r
                        .path("/book-service/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/book-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://BOOK-SERVICE"))

                .route("order-service-docs", r -> r
                        .path("/order-service/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/order-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://ORDER-SERVICE"))

                .build();
    }
}