package kr.co.felici.remembering.service;


import jakarta.persistence.EntityManager;
import kr.co.felici.remembering.config.UploadPaths;
import kr.co.felici.remembering.domain.BoardImage;
import kr.co.felici.remembering.domain.BoardVideo;
import kr.co.felici.remembering.domain.MemorialPost;
import kr.co.felici.remembering.domain.specification.MemorialPostSpecification;
import kr.co.felici.remembering.dto.AddMemorialPostDto;
import kr.co.felici.remembering.dto.MemorialPostResponse;
import kr.co.felici.remembering.dto.UpdateMemorialPostDto;
import kr.co.felici.remembering.exception.PostNotFoundException;
import kr.co.felici.remembering.repository.BoardImageRepository;
import kr.co.felici.remembering.repository.BoardVideoRepository;
import kr.co.felici.remembering.repository.MemorialPostRepository;
import kr.co.felici.remembering.util.ImageUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemorialPostService {

    private final MemorialPostRepository memorialPostRepository;
    private final BoardImageRepository imageRepository;
    private final BoardVideoRepository boardVideoRepository;
    private final EntityManager em;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    private final UploadPaths uploadPaths;
    private static final int PAGE_SIZE = 5;

    String mPhotoFile = "photoMFile";
    String mVideoFile = "videoMFile";


    public Boolean checkAuth(Long id) {
        MemorialPost thePost = this.findById(id);


        if (thePost.getPw() == "") {

        } else {

        }
        return false;
    }

    public MemorialPost findById(Long id) {
        return memorialPostRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }


    public Page<MemorialPost> getAll(int page, String kw) {
        PageRequest pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Order.desc("modifiedAt")));

        if (kw == null || kw.isBlank()) {
            return memorialPostRepository.findAll(pageable);
        }
        return memorialPostRepository.findAll(this.searchMemorialPost(kw), pageable);
    }

    @Transactional
    public void addPost(AddMemorialPostDto addMemorialPostDto)
            throws Exception {

        List<BoardImage> imageFileList = new ArrayList<>();
        List<BoardVideo> videoFileList = new ArrayList<>();

        if (addMemorialPostDto.getPw().equals("")) {
            return;
        }

        MemorialPost memorialPost = MemorialPost.builder()
                .writer(addMemorialPostDto.getWriter())
                .contents(addMemorialPostDto.getContents())
                .pw(bCryptPasswordEncoder.encode(addMemorialPostDto.getPw().strip()))
                .build();

        log.info("memorialPost: ", memorialPost);

        List<MemorialPostResponse> postsResponseList = null;


        saveMediaFiles(memorialPost, addMemorialPostDto.getPhoto(), mPhotoFile, imageFileList, videoFileList);
        saveMediaFiles(memorialPost, addMemorialPostDto.getVideo(), mVideoFile, imageFileList, videoFileList);

        if (!imageFileList.isEmpty()) {
            for (BoardImage image : imageFileList) {
                imageRepository.save(image);
            }
            memorialPost.addImages(imageFileList);
        }

        if (!videoFileList.isEmpty()) {
            for (BoardVideo video : videoFileList) {
                boardVideoRepository.save(video);
            }
        } else {
            log.info("No video file");
        }

        memorialPostRepository.save(memorialPost);

    }

    @Transactional
    public boolean deleteMemorialPost(Map<String, String> params) {
        Long id = Long.valueOf(params.get("id"));
        String pwd = params.get("pwd");

        MemorialPost thePost = this.findById(id);
        if (!bCryptPasswordEncoder.matches(pwd, thePost.getPw())) {
            log.info("비밀번호가 맞지 않아 게시글이 삭제되지 않았습니다. id={}", id);
            return false;
        }

        // DB를 지우기 전에 파일 경로를 먼저 모아 둔다
        List<String> imagePaths = thePost.getImages().stream().map(BoardImage::getPath).toList();
        List<String> videoPaths = thePost.getVideos().stream().map(BoardVideo::getPath).toList();

        memorialPostRepository.deleteById(id);

        for (String path : imagePaths) {
            deleteFileIfExists(uploadPaths.memorialPostImages() + File.separator + path);
            deleteFileIfExists(uploadPaths.memorialPostImages() + File.separator + "thumbnail" + File.separator + path);
        }
        for (String path : videoPaths) {
            deleteFileIfExists(uploadPaths.memorialPostVideos() + File.separator + path);
        }

        log.info("게시글 삭제 완료. id={}", id);
        return true;
    }

    @Transactional
    public boolean updateMemorialPost(UpdateMemorialPostDto updateMemorialPostDto) throws IOException {

        MemorialPost memorialPost = this.findById(updateMemorialPostDto.getId());

        if (!bCryptPasswordEncoder.matches(updateMemorialPostDto.getPw(), memorialPost.getPw())) {
            log.info("비밀번호가 맞지 않아 수정되지 않았습니다. id={}", updateMemorialPostDto.getId());
            return false;
        }

        List<BoardImage> imageFileList = new ArrayList<>();
        List<BoardVideo> videoFileList = new ArrayList<>();

        memorialPost.addContents(updateMemorialPostDto.getContents());

        saveMediaFiles(memorialPost, updateMemorialPostDto.getPhoto(), mPhotoFile, imageFileList, videoFileList);
        saveMediaFiles(memorialPost, updateMemorialPostDto.getVideo(), mVideoFile, imageFileList, videoFileList);
        imageFileList.forEach(em::persist);
        videoFileList.forEach(em::persist);

        return true;
    }

    public Specification<MemorialPost> searchMemorialPost(String kw) {
        Specification<MemorialPost> spec = (root, query, criteriaBuilder) -> null;

        if (kw != null && !kw.isEmpty()) {
            spec = spec.and(MemorialPostSpecification.likeContents(kw));
            spec = spec.or(MemorialPostSpecification.equalsWriter(kw));
        }

        return spec;
    }

    @Transactional
    public boolean deletePostImage(Map<String, String> params) {
        Long postId = Long.valueOf(params.get("postId"));
        Long imageId = Long.valueOf(params.get("mediaId"));
        String pwd = params.get("pwd");

        MemorialPost thePost = this.findById(postId);
        if (!bCryptPasswordEncoder.matches(pwd, thePost.getPw())) {
            log.info("비밀번호가 맞지 않아 이미지가 삭제되지 않았습니다. postId={}", postId);
            return false;
        }

        BoardImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지가 없습니다. id=" + imageId));

        // 다른 글의 이미지를 지우지 못하도록 소속 확인
        if (!image.getMemorialPost().getId().equals(postId)) {
            log.warn("게시글에 속하지 않은 이미지 삭제 시도. postId={}, imageId={}", postId, imageId);
            return false;
        }

        String path = image.getPath();
        imageRepository.delete(image);

        deleteFileIfExists(uploadPaths.memorialPostImages() + File.separator + path);
        deleteFileIfExists(uploadPaths.memorialPostImages() + File.separator + "thumbnail" + File.separator + path);
        return true;
    }

    @Transactional
    public boolean deletePostVideo(Map<String, String> params) {
        Long postId = Long.valueOf(params.get("postId"));
        Long videoId = Long.valueOf(params.get("mediaId"));
        String pwd = params.get("pwd");

        MemorialPost thePost = this.findById(postId);
        if (!bCryptPasswordEncoder.matches(pwd, thePost.getPw())) {
            log.info("비밀번호가 맞지 않아 동영상이 삭제되지 않았습니다. postId={}", postId);
            return false;
        }

        BoardVideo video = boardVideoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("동영상이 없습니다. id=" + videoId));

        if (!video.getMemorialPost().getId().equals(postId)) {
            log.warn("게시글에 속하지 않은 동영상 삭제 시도. postId={}, videoId={}", postId, videoId);
            return false;
        }

        String path = video.getPath();
        boardVideoRepository.delete(video);
        deleteFileIfExists(uploadPaths.memorialPostVideos() + File.separator + path);
        return true;
    }

    private void saveMediaFiles(MemorialPost memorialPost, List<MultipartFile> multipartFiles,
                                String typeOfFile, List<BoardImage> imageFileList,
                                List<BoardVideo> videoFileList) throws IOException {
        if (multipartFiles == null) {
            return;
        }

        boolean isPhoto = typeOfFile.equals("photoMFile");
        String basePath = isPhoto ? uploadPaths.memorialPostImages() : uploadPaths.memorialPostVideos();

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
                ImageUtility.createThumbnail(uploadFile);

                BoardImage image = BoardImage.builder()
                        .path(upLoadFileName)
                        .originalFilename(originalFileName)
                        .fileSize(multipartFile.getSize())
                        .build();
                imageFileList.add(image);
                memorialPost.addImage(image);
            } else {
                BoardVideo video = BoardVideo.builder()
                        .path(upLoadFileName)
                        .originalFilename(originalFileName)
                        .fileSize(multipartFile.getSize())
                        .build();
                videoFileList.add(video);
                memorialPost.addVideo(video);
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

    private void deleteFileIfExists(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.delete()) {
            log.warn("이미지 파일 삭제 실패: {}", file.getAbsolutePath());
        }
    }


}
