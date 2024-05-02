package kr.co.felici.remembering.service;

import jakarta.persistence.EntityManager;
import kr.co.felici.remembering.domain.BoardImage;
import kr.co.felici.remembering.repository.BoardImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * author: felici
 */
@Service
@RequiredArgsConstructor
public class BoardImageService {

//    String absolutePath = new File("").getAbsolutePath() + File.separator;

//    private final Path postImagesUploadPath =
//            Paths.get(absolutePath + "/media/memorialPosts/posts-images");
//            Paths.get("/home/felici/projects/remembering/media/memorialPosts/posts-images");

    private final BoardImageRepository imageRepository;
    private final EntityManager em;

    public List<BoardImage> findAllImages() {
        return imageRepository.findAll();
    }

    public BoardImage findById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow();
    }

    @Transactional
    public void deleteImage(Long imageId) {
        BoardImage image = em.find(BoardImage.class, imageId);

        imageRepository.delete(image);

    }






//    // To view an image
//    public byte[] getImage(String imageDirectory, String imageName) throws IOException {
//        Path imagePath = Path.of(imageDirectory, imageName);
//
//        if (Files.exists(imagePath)) {
//            byte[] imageBytes = Files.readAllBytes(imagePath);
//            return imageBytes;
//        } else {
//            return null; // Handle missing images
//        }
//    }
//
//    public Stream<Path> loadAll() {
//        try {
//            return Files.walk(this.rootPostImage, 1).filter(path -> !path.equals(this.rootPostImage)).map(this.rootPostImage::relativize);
//        } catch (IOException e) {
//            throw new RuntimeException("Could not load the files!");
//        }
//    }

}
