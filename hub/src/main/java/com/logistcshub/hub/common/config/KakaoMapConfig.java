package com.logistcshub.hub.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "kakaomap")
public class KakaoMapConfig {
    private String adminKey;
    private String mapUrl;
}
