package kr.co.felici.remembering.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 사이트마다 달라지는 값(기리는 분의 성함, 대표 사진)을 설정에서 읽는다.
 */
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.site")
public class SiteProperties {

    private final String memorialName;
    private final String heroImage;
    private final String profileImage;
}
