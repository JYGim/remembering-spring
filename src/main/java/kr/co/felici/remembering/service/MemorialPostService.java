package kr.co.felici.remembering.service;



import kr.co.felici.remembering.domain.MemorialPost;
import kr.co.felici.remembering.dto.MemorialPostDto;
import kr.co.felici.remembering.repository.MemorialPostRepository;
import kr.co.felici.remembering.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * author: felici
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MemorialPostService {

    private static final String CURR_PHOTO_PEPO_PATH = "//src/main//resources//static//post//files//photos";
    private static final String CURR_VIDEO_PEPO_PATH = "//src/main//resources//static//post//files//videos";

    private final MemorialPostRepository memorialPostRepository;
    private final PhotoRepository photoRepository;

    public MemorialPost findById(Long id) {
        return memorialPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public List<MemorialPost> getAllPost() {
        return memorialPostRepository.findAll();
    }

    public void addPost(MemorialPostDto memorialPostDto)
            throws Exception {

        MultipartFile photoMFile = memorialPostDto.getPhoto();
        MultipartFile videoMFile = memorialPostDto.getVideo();

        String originalPhotoFileName = photoMFile.getOriginalFilename();
        String originalVideoFileName = videoMFile.getOriginalFilename();

        String upLoadPhotoFileName = "";

        if(photoMFile.getSize() > 0) {
            upLoadPhotoFileName = createServerFileName(originalPhotoFileName);
//            upLoadPhotoFileName = createServerFileName(originalPhotoFileName);
        } else {
//            return;
        }

//        String upLoadPhotoFileName = createServerFileName(originalPhotoFileName);
        String upLoadVideoFileName = createServerFileName(originalVideoFileName);



//        String uploadPath = "resources//static//";
//        String uploadPath = "resources//static//post//files//photos";
//        String uploadPath = "//src//main//resources//static//post//files//photos";
        String uploadPath = "/home/felici/studyPj/spring-boot-study/blog-api-namsan22/src/main/resources/static/post/files/";
//        String uploadPath = "\\src\\main\\resources\\static\\post\\files\\photos";
//        String uploadPath = "/static/post/files/photos";
        // 2. 원본 파일 이름 알아오기
        String dpSavePath = "post/files";

        // 3. 파일 이름 중복되지 않게 이름 변경(서버에 저장할 이름) UUID 사용
        UUID uuid = UUID.randomUUID();
        // 4. 파일 생성
        File file1 = new File(uploadPath + File.separator + upLoadPhotoFileName);
//        File file1 = new File(uploadPath);
//        File file1 = new File(uploadPath);
        // 5. 서버로 전송
        if(!file1.exists()) {
            file1.mkdirs();
        }
//        if (!file1.getParentFile().exists()) {
//            file1.getParentFile().mkdirs();
//        }

//        if (!file1.exists() && !file1.createNewFile()) {
//            logger.error("Failed to create file");
//        }


        photoMFile.transferTo(new File(uploadPath + File.separator + upLoadPhotoFileName));



//        File pFile = new File(CURR_PHOTO_PEPO_PATH + "//" + upLoadPhotoFileName);
//        if(pFile.exists()) {
//            photoMFile.transferTo(pFile);
//        } else {
//            if(pFile.getParentFile().mkdirs()) {						//디렉토리를 만든 후 파일을 생성함
//                pFile.createNewFile();
//            }
//
//            photoMFile.transferTo(pFile);
//        }




//        photoMFile.transferTo(new File(CURR_PHOTO_PEPO_PATH +"//"+ upLoadPhotoFileName));





//        photoMFile.transferTo(new File(getFullPath(upLoadPhotoFileName, CURR_PHOTO_PEPO_PATH)));
//        photoMFile.transferTo(new File(getFullPath(upLoadPhotoFileName, CURR_PHOTO_PEPO_PATH)));
//        mFile.transferTo(new File(CURR_IMAGE_PEPO_PATH +"\\"+ originalFilename));

//        UUID uuid = UUID.randomUUID();
//
//        String fileName = projectPath + "/" + uuid.toString();
//
//        File saveFile = new File(fileName);
//        photoMFile.transferTo(projectPath);


        memorialPostRepository.save(MemorialPost.builder()
                .writer(memorialPostDto.getWriter())
                .contents(memorialPostDto.getContents())
                .photo(dpSavePath + File.separator + upLoadPhotoFileName)
                .video(upLoadVideoFileName)
                .pw(memorialPostDto.getPw())
                .build());


//        memorialPostDto.setPhoto("/files/" + fileName);


//        memorialPostRepository.save(memorialPostDto);


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


}
