package com.logistics.delivery.infrastructure.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate buildRestTemplate(RestTemplateBuilder builder){
        return builder.requestFactory(HttpComponentsClientHttpRequestFactory.class)
                .connectTimeout(Duration.ofSeconds(5000))
                .readTimeout(Duration.ofSeconds(5000))
                /*.requestFactory(true)*/
                .build();
    }
}

