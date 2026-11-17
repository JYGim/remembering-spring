package kr.co.felici.remembering.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

/**
 * 업로드 파일이 저장되는 폴더 경로를 한 곳에서 관리한다.
 */
@Component
public class UploadPaths {

    private final Path root;

    public UploadPaths(@Value("${app.upload-dir}") String uploadDir) {
        this.root = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    public String memorialPostImages() {
        return root.resolve("memorialPosts/images").toString();
    }

    public String memorialPostVideos() {
        return root.resolve("memorialPosts/posts-videos").toString();
    }

    public String letterImages() {
        return root.resolve("letters/images").toString();
    }

    public String letterVideos() {
        return root.resolve("letters/videos").toString();
    }

    public String albumPhotos() {
        return root.resolve("album/photo").toString();
    }

    public String albumVideos() {
        return root.resolve("album/video").toString();
    }
}
