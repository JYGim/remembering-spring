package kr.co.felici.remembering.service;


import jakarta.persistence.EntityManager;
import kr.co.felici.remembering.config.UploadPaths;
import kr.co.felici.remembering.domain.*;
import kr.co.felici.remembering.domain.specification.LetterSpecification;
import kr.co.felici.remembering.dto.AddLetterDto;
import kr.co.felici.remembering.dto.UpdateLetterDto;
import kr.co.felici.remembering.exception.LetterAccessDeniedException;
import kr.co.felici.remembering.exception.PostNotFoundException;
import kr.co.felici.remembering.repository.BoardImageRepository;
import kr.co.felici.remembering.repository.BoardVideoRepository;
import kr.co.felici.remembering.repository.LetterRepository;
import kr.co.felici.remembering.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class LetterService {

    private final UserRepository userRepository;
    private final LetterRepository letterRepository;
    private final BoardImageRepository imageRepository;
    private final BoardVideoRepository boardVideoRepository;
    private final EntityManager em;

    private final UploadPaths uploadPaths;
    private static final int PAGE_SIZE = 5;

    String mPhotoFile = "photoMFile";
    String mVideoFile = "videoMFile";

    public Letter findById(Long letterId) {
        return letterRepository.findById(letterId)
                .orElseThrow(() -> new PostNotFoundException(letterId));
    }

    @Transactional(readOnly = true)
    public Letter findOwnedLetter(Long id, User user) {
        Letter letter = this.findById(id);
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = letter.getUser() != null && letter.getUser().getId().equals(user.getId());
        if (!isAdmin && !isOwner) {
            throw new LetterAccessDeniedException(id);
        }
        return letter;
    }

    public Page<Letter> getAll(int page, String kw) {
        PageRequest pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Order.desc("modifiedAt")));

        if (kw == null || kw.isBlank()) {
            return letterRepository.findAll(pageable);
        }
        return letterRepository.findAll(LetterSpecification.likeContents(kw), pageable);
    }

    @Transactional
    public void addLetter(AddLetterDto addLetterDto) throws Exception {

        List<BoardImage> imageFileList = new ArrayList<>();
        List<BoardVideo> videoFileList = new ArrayList<>();

        Optional<User> byEmail = userRepository.findByEmail(addLetterDto.getEmail());


        Letter letter = Letter.builder()
                .user(byEmail.get())
                .contents(addLetterDto.getContents())
                .build();

        saveMediaFiles(letter, addLetterDto.getPhoto(), mPhotoFile, imageFileList, videoFileList);
        saveMediaFiles(letter, addLetterDto.getVideo(), mVideoFile, imageFileList, videoFileList);

        if (!imageFileList.isEmpty()) {
            for (BoardImage image : imageFileList) {
                log.info(image.getPath(), image.getOriginalFilename(), image.getLetter(), image.getFileSize());
                imageRepository.save(image);
            }
        }

        if (!videoFileList.isEmpty()) {
            for (BoardVideo video : videoFileList) {
                log.info(video.getPath());
                boardVideoRepository.save(video);
            }
        }


        letterRepository.save(letter);
    }


    @Transactional
    public Letter updateLetter(UpdateLetterDto updateLetterDto, User user) throws IOException {

        List<BoardImage> imageFileList = new ArrayList<>();
        List<BoardVideo> videoFileList = new ArrayList<>();

        Letter letter = this.findOwnedLetter(updateLetterDto.getId(), user);
        letter.addContents(updateLetterDto.getContents());

        saveMediaFiles(letter, updateLetterDto.getPhoto(), mPhotoFile, imageFileList, videoFileList);
        saveMediaFiles(letter, updateLetterDto.getVideo(), mVideoFile, imageFileList, videoFileList);
        imageFileList.forEach(em::persist);
        videoFileList.forEach(em::persist);

        return letter;
    }



    private void saveMediaFiles(Letter letter, List<MultipartFile> multipartFiles,
                                String typeOfFile, List<BoardImage> imageFileList,
                                List<BoardVideo> videoFileList) throws IOException {
        if (multipartFiles == null) {
            return;
        }

        boolean isPhoto = typeOfFile.equals("photoMFile");
        String basePath = isPhoto ? uploadPaths.letterImages() : uploadPaths.letterVideos();

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }

            String originalFileName = multipartFile.getOriginalFilename();
            String upLoadFileName = createServerFileName(originalFileName);
            File uploadFile = new File(basePath + File.separator + upLoadFileName);

            Files.createDirectories(uploadFile.toPath().getParent());
            multipartFile.transferTo(uploadFile);

            if (isPhoto) {
                BoardImage image = BoardImage.builder()
                        .path(upLoadFileName)
                        .originalFilename(originalFileName)
                        .fileSize(multipartFile.getSize())
                        .build();
                imageFileList.add(image);
                letter.addImage(image);
            } else {
                BoardVideo video = BoardVideo.builder()
                        .path(upLoadFileName)
                        .originalFilename(originalFileName)
                        .fileSize(multipartFile.getSize())
                        .build();
                videoFileList.add(video);
                letter.addVideo(video);
            }
        }
    }

    @Transactional
    public void deleteLetter(Long id, User user) {
        Letter theLetter = this.findOwnedLetter(id, user);

        List<String> imagePaths = theLetter.getImages().stream().map(BoardImage::getPath).toList();
        List<String> videoPaths = theLetter.getVideos().stream().map(BoardVideo::getPath).toList();

        letterRepository.delete(theLetter);

        for (String path : imagePaths) {
            deleteFileIfExists(uploadPaths.letterImages() + File.separator + path);
        }
        for (String path : videoPaths) {
            deleteFileIfExists(uploadPaths.letterVideos() + File.separator + path);
        }
        log.info("편지 삭제 완료. id={}", id);
    }

    @Transactional
    public void deleteLetterImage(Long letterId, Long imageId, User user) {
        this.findOwnedLetter(letterId, user);

        BoardImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지가 없습니다. id=" + imageId));
        if (image.getLetter() == null || !image.getLetter().getId().equals(letterId)) {
            throw new LetterAccessDeniedException(letterId);
        }

        String path = image.getPath();
        imageRepository.delete(image);
        deleteFileIfExists(uploadPaths.letterImages() + File.separator + path);
    }

    @Transactional
    public void deleteLetterVideo(Long letterId, Long videoId, User user) {
        this.findOwnedLetter(letterId, user);

        BoardVideo video = boardVideoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("동영상이 없습니다. id=" + videoId));
        if (video.getLetter() == null || !video.getLetter().getId().equals(letterId)) {
            throw new LetterAccessDeniedException(letterId);
        }

        String path = video.getPath();
        boardVideoRepository.delete(video);
        deleteFileIfExists(uploadPaths.letterVideos() + File.separator + path);
    }

    private void deleteFileIfExists(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.delete()) {
            log.warn("파일 삭제 실패: {}", file.getAbsolutePath());
        }
    }

    public String getFullPath(String filename, String fileDir) {
        return fileDir + filename;
    }

    //uuid 생성해서 뒤에 원래파일명의 확장자명 붙이기
    private String createServerFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    //원래 파일명에서 확장자 뽑기(.jpg, .png ...)
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
