package kr.co.felici.remembering.service;



import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import kr.co.felici.remembering.domain.BoardImage;
import kr.co.felici.remembering.domain.Letter;
import kr.co.felici.remembering.domain.MemorialPost;
import kr.co.felici.remembering.domain.BoardVideo;
import kr.co.felici.remembering.domain.specification.MemorialPostSpecification;
import kr.co.felici.remembering.dto.*;
import kr.co.felici.remembering.repository.BoardVideoRepository;
import kr.co.felici.remembering.repository.BoardImageRepository;
import kr.co.felici.remembering.repository.MemorialPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * author: felici
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemorialPostService {
//    private static final String CURR_PHOTO_PEPO_PATH = "//src/main//resources//static//post//files//photos";
//    private static final String CURR_VIDEO_PEPO_PATH = "//src/main//resources//static//post//files//videos";

    private final MemorialPostRepository memorialPostRepository;
    private final BoardImageRepository imageRepository;
    private final BoardVideoRepository boardVideoRepository;
    private final EntityManager em;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    private String originalFileName = "";
    String upLoadFileName = "";
    File uploadFile = null;
    String absolutePath = new File("").getAbsolutePath() + File.separator;
    String uploadRootPath = absolutePath + "media/memorialPosts";
    String basePath = "";
    String pathForImages = uploadRootPath + File.separator + "images";
    String pathForImagesDir = uploadRootPath + File.separator + "images";

    Path uploadImagePath = Path.of(pathForImagesDir);

    String pathForVideos = uploadRootPath + File.separator + "posts-videos";
    String dbSavebasePath = "";
    String dbSaveImagePath = "images";
    String dbSaveVideoPath = "videos";
    String pathArg = null;
    String mPhotoFile = "photoMFile";
    String mVideoFile = "videoMFile";

    Boolean isSuccess = false;

    String imagePathToDelete = "";

    String baseFilePath = "/home/felici/studyPj/spring-boot-study/remembering/media";
    String lettersFilePath = baseFilePath + "/letters";
    String memorialPostsFilePath = baseFilePath + "/memorialPosts";



    public Boolean checkAuth(Long id) {
        Optional<MemorialPost> optionalThePost = memorialPostRepository.findById(id);
        MemorialPost thePost = optionalThePost.get();



        if(thePost.getPw() == "") {

        } else {

        }
        return false;
    }

    public MemorialPost findById(Long id) {
        return memorialPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }
    public Page<MemorialPost> getAll(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("modifiedAt"));
        PageRequest pageable = PageRequest.of(page, 2, Sort.by(sorts));

        if(kw != null && kw.equals("")) {
            Page<MemorialPost> posts = memorialPostRepository.findAll(pageable);
            log.info("log, posts: " + posts);
            log.info("log, kw: " + kw);
            return posts;

        } else if (kw != null && !kw.isEmpty()) {
            Specification<MemorialPost> spec = this.searchMemorialPost(kw);
            Page<MemorialPost> posts = memorialPostRepository.findAll(spec, pageable);
            log.info("log, spec: " + spec);
            log.info("log, kw가 비어 있지 않아요");
            log.info("log, posts의 수: " + posts.getTotalElements());
            log.info("log, kw: " + kw);
            return posts;
        }

        return null;
    }
    @Transactional
    public void addPost(AddMemorialPostDto addMemorialPostDto)
            throws Exception {
        List<BoardImage> imageFileList = new ArrayList<>();
        List<BoardVideo> videoFileList = new ArrayList<>();

        MemorialPost memorialPost = MemorialPost.builder()
                .writer(addMemorialPostDto.getWriter())
                .contents(addMemorialPostDto.getContents())
                .pw(bCryptPasswordEncoder.encode(addMemorialPostDto.getPw()))
                .build();

        log.info("memorialPost: ", memorialPost);

//        List<MultipartFile> photoMultipartFileList = addMemorialPostDto.getPhoto();
//        List<MultipartFile> videoMultipartFileList = addMemorialPostDto.getVideo();

        addmediaFile(memorialPost, addMemorialPostDto, mPhotoFile, imageFileList, videoFileList);
        addmediaFile(memorialPost, addMemorialPostDto, mVideoFile, imageFileList, videoFileList);

        if(!imageFileList.isEmpty()) {
            for(BoardImage image: imageFileList) {
                imageRepository.save(image);
            }
        }

        if(!videoFileList.isEmpty()) {
            for(BoardVideo video: videoFileList) {
                boardVideoRepository.save(video);
            }
        } else {
            log.info("No video file");
        }

        memorialPostRepository.save(memorialPost);


    }

    public String deleteMemorialPost(Map<String, String> params) {

        log.info("params: " + params);
        String id = params.get("id");
        String pwd = params.get("pwd");

        MemorialPost thePost = this.findById(Long.valueOf(id));
        String password = thePost.getPw();

        log.info("password: " + password);
        log.info("pwd: " + bCryptPasswordEncoder.encode(pwd));

        if(bCryptPasswordEncoder.matches(pwd, password)) {

            List<BoardImage> images = thePost.getImages();
            List<BoardVideo> videos = thePost.getVideos();

            if (!images.isEmpty()) {
                for (BoardImage image : images) {
                    try {
                        File file = new File(pathForImages + File.separator + image.getPath());
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
                        File file = new File(pathForVideos + File.separator + video.getPath());
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
            memorialPostRepository.deleteById(Long.valueOf(id));

            log.info("삭제 성공!!!" + id);
            return id;
        } else {
            log.info("비번 안맞음???" + id);
            return "-1";
        }

    }

    @Transactional
    public MemorialPost updateMemorialPost(UpdateMemorialPostDto updateMemorialPostDto) throws IOException {

        String password = updateMemorialPostDto.getPw();
        log.info("password: " + password);
        log.info("isSuccess: " + isSuccess);

        MemorialPost memorialPost = em.find(MemorialPost.class, updateMemorialPostDto.getId());

        if(bCryptPasswordEncoder.matches(password, memorialPost.getPw())) {

            List<BoardImage> imageFileList = new ArrayList<>();
            List<BoardVideo> videoFileList = new ArrayList<>();

            memorialPost.addContents(updateMemorialPostDto.getContents());

            updateMediaFile(memorialPost, updateMemorialPostDto, mPhotoFile, imageFileList, videoFileList);
            updateMediaFile(memorialPost, updateMemorialPostDto, mVideoFile, imageFileList, videoFileList);

            isSuccess = true;

        } else {
            log.info("비번이 맞지 않아 수정되지 않았습니다. !!!");
            isSuccess = false;
        }

        return memorialPost;
    }

    public Specification<MemorialPost> searchMemorialPost(String kw) {
        Specification<MemorialPost> spec = (root, query, criteriaBuilder) -> null;

        if(kw != null && !kw.isEmpty()) {
            spec = spec.and(MemorialPostSpecification.likeContents(kw));
            spec = spec.or(MemorialPostSpecification.equalsWriter(kw));

            log.info("log, spec: " + spec);

        }

        return spec;
    }

    public MemorialPost deletePostImage(Map<String, String> params) {

        String postId = params.get("postId");
        String pwd = params.get("pwd");
        String imageId = params.get("imageId");

        log.info("log, params-postId, pwd: " + postId + ", " + pwd);
        log.info("log, params-imageId: " + imageId);

        MemorialPost thePost = this.findById(Long.parseLong(postId));
        String pw = thePost.getPw();


//        List<BoardImage> images = thePost.getImages();

        if(bCryptPasswordEncoder.matches(pwd, pw)) {
//            for(BoardImage image : images) {
//                imageRepository.deleteById(image.getId());
//
//                deleteFile(image, null, memorialPostsFilePath);
//                isSuccess = true;
//
//            }
            imagePathToDelete = getDeletedImageName(imageId);
            imageRepository.deleteById(Long.parseLong(imageId));
            isSuccess = true;

        } else {
            log.info("log, 비번이 맞지 않아 수정되지 않았습니다. !!!");
            isSuccess = false;
        }

        return thePost;
    }



    private String getDeletedImageName(String imageId) {
        Optional<BoardImage> byId = imageRepository.findById(Long.parseLong(imageId));
        String imageName = byId.get().getPath();
        return imageName;
    }

    public String passDeletedImagePath() {
        String passDeletedImagePath = imagePathToDelete;
        imagePathToDelete = "";
        return passDeletedImagePath;
    }

    public Boolean succeed() {
        return isSuccess;
    }

    public void deleteFile(BoardImage image, BoardVideo video, String type) {
        if(image != null) {
            try {
                File file = new File( type + File.separator + "images" + File.separator + image.getPath());

                if(file.exists()) {
                    boolean result = file.delete();
                    if(result) {
                        log.info("파일 지웠어요.");
                    }
                } else {
                    log.info("파일 없어요.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(video != null) {
            try {
                File file = new File( type + File.separator + "videos" + File.separator + video.getPath());

                if(file.exists()) {
                    boolean result = file.delete();
                    if(result) {
                        System.out.println("파일 지웠어요.");
                    }
                } else {
                    System.out.println("파일 없어요.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addmediaFile(MemorialPost memorialPost, AddMemorialPostDto addMemorialPostDto,
                              String typeOfFile, List<BoardImage> imageFileList,
                              List<BoardVideo> videoFileList) throws IOException {
        List<MultipartFile> multipartFiles = null;

        switch (typeOfFile) {
            case "photoMFile":
                multipartFiles = addMemorialPostDto.getPhoto();
                basePath = pathForImages;
                dbSavebasePath = dbSaveImagePath;
                break;
            case "videoMFile":
                multipartFiles = addMemorialPostDto.getVideo();
                basePath = pathForVideos;
                dbSavebasePath = dbSaveVideoPath;
                break;
        }
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (multipartFile.getSize() >= 1) {
                    originalFileName = multipartFile.getOriginalFilename();

                    upLoadFileName = createServerFileName(originalFileName);

                    uploadFile = new File(basePath + File.separator + upLoadFileName);

                    Path filePath = uploadImagePath.resolve(upLoadFileName);

                    if(!Files.exists(uploadImagePath)) {
                        Files.createDirectories(uploadImagePath);
                    }

//                    Files.copy(image)

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
                        memorialPost.addImage(image);
                    } else if (typeOfFile.equals("videoMFile")) {
                        BoardVideo video = BoardVideo.builder()
                                .path(pathArg)
                                .originalFilename(originalFileName)
                                .fileSize(multipartFile.getSize())
                                .build();
                        videoFileList.add(video);
                        memorialPost.addVideo(video);
                    }
                }
            }
        }
    }

    private void updateMediaFile(MemorialPost memorialPost, UpdateMemorialPostDto updateMemorialPostDto,
                                 String typeOfFile, List<BoardImage> imageFileList,
                                 List<BoardVideo> videoFileList) throws IOException {
        List<MultipartFile> multipartFiles = null;

        switch (typeOfFile) {
            case "photoMFile":
                multipartFiles = updateMemorialPostDto.getPhoto();
                basePath = pathForImages;
                dbSavebasePath = dbSaveImagePath;
                break;
            case "videoMFile":
                multipartFiles = updateMemorialPostDto.getVideo();
                basePath = pathForVideos;
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
                        memorialPost.addImage(image);
                        em.persist(image);
                    } else if (typeOfFile.equals("videoMFile")) {
                        BoardVideo video = BoardVideo.builder()
                                .path(pathArg)
                                .originalFilename(originalFileName)
                                .fileSize(multipartFile.getSize())
                                .build();
                        videoFileList.add(video);
                        memorialPost.addVideo(video);
                        em.persist(video);
                    }
                }
            }
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
//        return "good" + "." + ext;
    }

    //원래 파일명에서 확장자 뽑기(.jpg, .png ...)
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public String generateFileUrl(String baseUrl, String fileName) {
        return String.format(URL_FORMAT, baseUrl, fileName);
    }


    private static final String URL_FORMAT = "%s/%s";




}
