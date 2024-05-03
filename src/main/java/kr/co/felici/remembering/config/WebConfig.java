package kr.co.felici.remembering.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * author: felici
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/letters/images/**", "/memorialPosts/images/**" )
        registry.addResourceHandler("/memorialPosts/images/**", "/letters/images/**",
                                                    "/media/**")
//        registry.addResourceHandler("/letters/images/**")
//                .addResourceLocations("file:///home/felici/studyPj/spring-boot-study/remembering/media/")
                .addResourceLocations(
//                        "file:///home/felici/studyPj/spring-boot-study/remembering/media/letters/images/",
//                        "file:///home/felici/studyPj/spring-boot-study/remembering/media/memorialPosts/images/")
                            "file:///home/felici/projects/remembering/media/letters/images/",
                            "file:///home/felici/projects/remembering/media/memorialPosts/images/",
                            "file:///home/felici/projects/remembering/media/")
//                .addResourceLocations(
//                        "file:///home/felici/studyPj/spring-boot-study/remembering/media/letters/images/")

                .setCachePeriod(0);
    }
}








