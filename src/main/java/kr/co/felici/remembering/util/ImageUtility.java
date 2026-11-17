package kr.co.felici.remembering.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class ImageUtility {

    private static final int THUMBNAIL_SIZE = 500;

    private ImageUtility() {
    }

    /**
     * 원본 이미지가 있는 폴더의 thumbnail 폴더에 같은 이름으로 썸네일을 만든다.
     */
    public static void createThumbnail(File originalFile) throws IOException {
        if (!originalFile.exists()) {
            log.warn("썸네일을 만들 원본 파일이 없습니다: {}", originalFile.getAbsolutePath());
            return;
        }

        File thumbnailDir = new File(originalFile.getParentFile(), "thumbnail");
        Files.createDirectories(thumbnailDir.toPath());

        Thumbnails.of(originalFile)
                .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                .toFile(new File(thumbnailDir, originalFile.getName()));
    }
}