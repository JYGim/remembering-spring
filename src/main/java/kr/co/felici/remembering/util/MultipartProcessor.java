package kr.co.felici.remembering.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * author: felici
 */
@Component
public class MultipartProcessor {

//    public void store(MultipartFile file) {
//        try {
//            if (file.isEmpty()) { // 파일 검증
//                throw new StorageException("Failed to store empty file.");
//            }
//            String fileName = uuidGenerator.generate() + DELIMITER + file.getOriginalFilename(); // 파일 이름 생성
//            Path destinationFile = rootLocation.resolve(Paths.get(fileName)) // 저장할 경로
//                    .normalize()
//                    .toAbsolutePath();
//            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) { // 경로 유효성 검증
//                // This is a security check
//                throw new StorageException(
//                        "Cannot store file outside current directory.");
//            }
//            try (InputStream inputStream = file.getInputStream()) { // 파일 저장
//                Files.copy(inputStream, destinationFile,
//                        StandardCopyOption.REPLACE_EXISTING);
//            }
//        }
//        catch (IOException e) {
//            throw new StorageException("Failed to store file.", e);
//        }
//    }


    // 필자가 작성한 store 메소드
//    public FileSaveResult store(MultipartFile file) {
//        validateFile(file); // 1번
//        String fileName = fileGeneratorUtil.generateFileName(file.getOriginalFilename()); // 2번
//        Path absolutePath = fileGeneratorUtil.generateAbsolutePath(fileName);
//
//        fileGenerator.save(file, absolutePath); // 3번
//        return FileSaveResult.builder()
//                .url(fileGeneratorUtil.generateFileUrl(imageConfig.getBaseUrl(), fileName))
//                .fileName(fileName)
//                .originalFileName(file.getOriginalFilename())
//                .build();
//    }

}
