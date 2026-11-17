package kr.co.felici.remembering.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UploadPaths uploadPaths;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        serve(registry, "/memorialPosts/images/**", uploadPaths.memorialPostImages());
        serve(registry, "/memorialPosts/videos/**", uploadPaths.memorialPostVideos());
        serve(registry, "/letters/images/**", uploadPaths.letterImages());
        serve(registry, "/letters/videos/**", uploadPaths.letterVideos());
        serve(registry, "/album/photo/**", uploadPaths.albumPhotos());
        serve(registry, "/album/video/**", uploadPaths.albumVideos());
    }

    private void serve(ResourceHandlerRegistry registry, String urlPattern, String directory) {
        registry.addResourceHandler(urlPattern)
                .addResourceLocations("file:" + directory + "/")
                .setCachePeriod(0);
    }
}