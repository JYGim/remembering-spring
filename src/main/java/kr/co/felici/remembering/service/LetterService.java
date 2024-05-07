package kr.co.felici.remembering.service;


import jakarta.persistence.EntityManager;
import kr.co.felici.remembering.domain.*;
import kr.co.felici.remembering.domain.specification.LetterSpecification;
import kr.co.felici.remembering.domain.specification.MemorialPostSpecification;
import kr.co.felici.remembering.dto.AddLetterDto;
import kr.co.felici.remembering.dto.LetterDto;
import kr.co.felici.remembering.dto.UpdateLetterDto;
import kr.co.felici.remembering.repository.BoardVideoRepository;
import kr.co.felici.remembering.repository.BoardImageRepository;
import kr.co.felici.remembering.repository.LetterRepository;
import kr.co.felici.remembering.repository.UserRepository;
import kr.co.felici.remembering.util.MultipartProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.List;

/**
 * author: felici
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterService {

    private final UserRepository userRepository;
    private final LetterRepository letterRepository;
    private final BoardImageRepository imageRepository;
    private final BoardVideoRepository boardVideoRepository;
    private final EntityManager em;

    String originalFileName = "";
    String upLoadFileName = "";
    File uploadFile = null;
    String absolutePath = new File("").getAbsolutePath() + File.separator;
    String uploadlettersRootPath = absolutePath + "media/letters";
    String basePath = "";
    String lettersPathForImages = uploadlettersRootPath + File.separator + "images";
    String lettersPathForVideos = uploadlettersRootPath + File.separator + "videos";
    String dbSavebasePath = "";
    String dbSaveImagePath = "images";
    String dbSaveVideoPath = "videos";
    String pathArg = null;
    String mPhotoFile = "photoMFile";
    String mVideoFile = "videoMFile";

    public Letter findById(Long letterId) {
        return letterRepository.findById(letterId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + letterId));
    }

    public Page<Letter> getAll(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("modifiedAt"));
        PageRequest pageable = PageRequest.of(page, 2, Sort.by(sorts));

        if (kw != null && kw.equals("")) {
            Page<Letter> letters = letterRepository.findAll(pageable);
            log.info("log, letters: " + letters);
            log.info("log, kw: " + kw);
            return letters;

        } else if (kw != null && !kw.isEmpty()) {
            Specification<Letter> spec = this.searchLetter(kw, pageable);
            Page<Letter> letters = letterRepository.findAll(spec, pageable);
            log.info("log, spec: " + spec);
            log.info("log, kw가 비어 있지 않아요");
            log.info("log, letters의 수: " + letters.getTotalElements());
            log.info("log, kw: " + kw);

            return letters;
        }

        return null;
    }

    // user 검색 기능 추가해야 함.
    private Specification<Letter> searchLetter(String kw, Pageable pageable) {

        Specification<Letter> spec = (root, query, criteriaBuilder) -> null;

//        Page<Letter> allByUserEmail = letterRepository.findAllByUser(kw, pageable);

        if (kw != null && !kw.isEmpty()) {
            spec = spec.and(LetterSpecification.likeContents(kw));
//            spec = spec.or(LetterSpecification.likeUserEmail(kw));

            log.info("log, spec: " + spec);

        }

        return spec;
    }

    @Transactional
    public void addLetter(AddLetterDto addLetterDto) throws Exception {
        log.info(absolutePath);

        List<BoardImage> imageFileList = new ArrayList<>();
        List<BoardVideo> videoFileList = new ArrayList<>();

        Optional<User> byEmail = userRepository.findByEmail(addLetterDto.getEmail());


        Letter letter = Letter.builder()
                .user(byEmail.get())
                .contents(addLetterDto.getContents())
                .build();


        addMediaFile(letter, addLetterDto, mPhotoFile, imageFileList, videoFileList);
        addMediaFile(letter, addLetterDto, mVideoFile, imageFileList, videoFileList);

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
//        em.persist(letter);
    }


    @Transactional
    public Letter updateLetter(UpdateLetterDto updateLetterDto) throws IOException {

        List<BoardImage> imageFileList = new ArrayList<>();
        List<BoardVideo> videoFileList = new ArrayList<>();

        Letter letter = em.find(Letter.class, updateLetterDto.getId());
        letter.addContents(updateLetterDto.getContents());

        updateMultimedia(letter, updateLetterDto, mPhotoFile, imageFileList, videoFileList);
        updateMultimedia(letter, updateLetterDto, mVideoFile, imageFileList, videoFileList);

        return letter;
    }

    private void addMediaFile(Letter letter, AddLetterDto addLetterDto,
                              String typeOfFile, List<BoardImage> imageFileList,
                              List<BoardVideo> videoFileList) throws IOException {
        List<MultipartFile> multipartFiles = null;

        switch (typeOfFile) {
            case "photoMFile":
                multipartFiles = addLetterDto.getPhoto();
                basePath = lettersPathForImages;
                dbSavebasePath = dbSaveImagePath;
                break;
            case "videoMFile":
                multipartFiles = addLetterDto.getVideo();
                basePath = lettersPathForVideos;
                dbSavebasePath = dbSaveVideoPath;
                break;
        }
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (multipartFile.getSize() >= 1) {
                    originalFileName = multipartFile.getOriginalFilename();

                    upLoadFileName = createServerFileName(originalFileName);


                    uploadFile = new File(basePath + File.separator + upLoadFileName);
                    if (!uploadFile.exists()) {
                        uploadFile.mkdirs();
                    }
                    multipartFile.transferTo(uploadFile);

//                    pathArg = dbSavebasePath + File.separator + upLoadFileName;
                    pathArg = upLoadFileName;

                    if (typeOfFile.equals("photoMFile")) {
                        BoardImage image = BoardImage.builder()
                                .path(pathArg)
                                .originalFilename(originalFileName)
                                .fileSize(multipartFile.getSize())
                                .build();
                        imageFileList.add(image);
                        letter.addImage(image);
                    } else if (typeOfFile.equals("videoMFile")) {
                        BoardVideo video = BoardVideo.builder()
                                .path(pathArg)
                                .originalFilename(originalFileName)
                                .fileSize(multipartFile.getSize())
                                .build();
                        videoFileList.add(video);
                        letter.addVideo(video);
                    }
                }
            }
        }
    }

    private void updateMultimedia(Letter letter, UpdateLetterDto updateLetterDto, String typeOfFile,
                                  List<BoardImage> imageFileList,
                                  List<BoardVideo> videoFileList) throws IOException {
        List<MultipartFile> multipartFiles = null;

        switch (typeOfFile) {
            case "photoMFile":
                multipartFiles = updateLetterDto.getPhoto();
                basePath = lettersPathForImages;
                dbSavebasePath = dbSaveImagePath;
                break;
            case "videoMFile":
                multipartFiles = updateLetterDto.getVideo();
                basePath = lettersPathForVideos;
                dbSavebasePath = dbSaveVideoPath;
                break;
        }
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (multipartFile.getSize() >= 1) {
                    originalFileName = multipartFile.getOriginalFilename();

                    upLoadFileName = createServerFileName(originalFileName);


                    uploadFile = new File(basePath + File.separator + upLoadFileName);
                    if (!uploadFile.exists()) {
                        uploadFile.mkdirs();
                    }
                    multipartFile.transferTo(uploadFile);

//                    pathArg = dbSavebasePath + File.separator + upLoadFileName;
                    pathArg = upLoadFileName;

                    if (typeOfFile.equals("photoMFile")) {
                        BoardImage image = BoardImage.builder()
                                .path(pathArg)
                                .originalFilename(originalFileName)
                                .fileSize(multipartFile.getSize())
                                .build();
                        imageFileList.add(image);
                        letter.addImage(image);
                        em.persist(image);
                    } else if (typeOfFile.equals("videoMFile")) {
                        BoardVideo video = BoardVideo.builder()
                                .path(pathArg)
                                .originalFilename(originalFileName)
                                .fileSize(multipartFile.getSize())
                                .build();
                        videoFileList.add(video);
                        letter.addVideo(video);
                        em.persist(video);
                    }
                }
            }
        }
    }

    public Long deleteLetter(Long id) {

        Letter theLetter = this.findById(Long.valueOf(id));

        List<BoardImage> images = theLetter.getImages();
        List<BoardVideo> videos = theLetter.getVideos();

        if (!images.isEmpty()) {
            for (BoardImage image : images) {
                try {
                    File file = new File(lettersPathForImages + File.separator + image.getPath());
                    log.info(file.getAbsolutePath());
                    if (file.exists()) {
                        boolean result = file.delete();
                        if (result) {
                            log.info("파일 삭제 성공");
                        } else {
                            log.info("파일 삭제 실패");
                        }
                    } else {
                        log.info("파일이 없어요!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        if (!videos.isEmpty()) {
            for (BoardVideo video : videos) {
                try {
                    File file = new File(lettersPathForVideos + File.separator + video.getPath());
                    log.info(file.getAbsolutePath());
                    if (file.exists()) {
                        boolean result = file.delete();
                        if (result) {
                            log.info("파일 삭제 성공");
                        } else {
                            log.info("파일 삭제 실패");
                        }
                    } else {
                        log.info("파일이 없어요!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        letterRepository.deleteById(Long.valueOf(id));

        log.info("삭제 성공!!!" + id);
        return id;


    }

//    @Transactional
//    public void deleteLetterImage(Long id) {
//        Letter letter = em.find(Letter.class, id);
//
//
//
//
//    }

    public String getFullPath(String filename, String fileDir) {
        return fileDir + filename;
    }

    //uuid 생성해서 뒤에 원래파일명의 확장자명 붙이기
    private String createServerFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
//        return "good" + "." + ext;
    }

    //원래 파일명에서 확장자 뽑기(.jpg, .png ...)
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


//    private List<> getList(AddLetterDto addLetterDto, String type) throws IOException {
//
//
//        if(Objects.equals(type, "photo")) {
//
//
//            if(!addLetterDto.getPhoto().isEmpty()) {
//                for(MultipartFile multipartFile: addLetterDto.getPhoto()) {
//                    if(multipartFile.getSize() >= 1) {
//                        originalFileName = multipartFile.getOriginalFilename();
//                        upLoadFileName = createServerFileName(originalFileName);
//                        mediaFile = new File(uploadPath + File.separator + type + File.separator + upLoadFileName);
////                      uploadFile = new File(uploadPath + File.separator + "photo" + File.separator + upLoadPhotoFileName);
//                        if(!mediaFile.exists()) {
//                            mediaFile.mkdirs();
//                        }
//                        multipartFile.transferTo(new File(uploadPath + File.separator + type + File.separator + upLoadFileName));
//                    }
//                    if(upLoadFileName != "") {
//                        photoArg = uploadPath + File.separator + type + File.separator + upLoadFileName;
//
//                        Image image = new Image(
//                                photoArg,
//                                originalFileName,
//                                multipartFile.getSize()
//                        );
//
//                        imageFileList.add(image);
//                        return imageFileList;
//                    }
//                }
//            }
//        }
//        return null;
//
//    }


}
