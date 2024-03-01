package kr.co.felici.remembering.service;



import kr.co.felici.remembering.domain.Letter;
import kr.co.felici.remembering.dto.AddLetterDto;
import kr.co.felici.remembering.repository.LetterRepository;
import lombok.RequiredArgsConstructor;
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
public class LetterService {

    private final LetterRepository letterRepository;

    public List<Letter> getAllPost() {
        return letterRepository.findAll();
    }

    public void addLetter(AddLetterDto addLetterDto) throws Exception {

        MultipartFile photoMFile = addLetterDto.getPhoto();
        MultipartFile videoMFile = addLetterDto.getVideo();
        String originalPhotoFileName = "";
        String originalVideoFileName = "";
        String upLoadPhotoFileName = "";
        String upLoadVideoFileName = "";

        String uploadPath = "/home/felici/studyPj/spring-boot-study/blog-api-namsan22/src/main/resources/static/post/files/";

        File photoFile = null;
        File videoFile = null;


        if(photoMFile.getSize() > 0) {
            originalPhotoFileName = photoMFile.getOriginalFilename();
            upLoadPhotoFileName = createServerFileName(originalPhotoFileName);
            photoFile = new File(uploadPath + File.separator + "photo" + File.separator + upLoadPhotoFileName);

            if(!photoFile.exists()) {
                photoFile.mkdirs();
            }
            photoMFile.transferTo(new File(uploadPath + File.separator + "photo" + File.separator + upLoadPhotoFileName));
        }
        if(videoMFile.getSize() > 0) {
            originalVideoFileName = videoMFile.getOriginalFilename();
            upLoadVideoFileName = createServerFileName(originalVideoFileName);
            videoFile = new File(uploadPath + "video" + File.separator + upLoadVideoFileName);

            if(!videoFile.exists()) {
                videoFile.mkdirs();
            }

            videoMFile.transferTo(new File(uploadPath + "video" + File.separator + upLoadPhotoFileName));
        }

//        String uploadPath = "resources//static//";
//        String uploadPath = "resources//static//post//files//photos";
//        String uploadPath = "//src//main//resources//static//post//files//photos";
//        String uploadPath = "\\src\\main\\resources\\static\\post\\files\\photos";
//        String uploadPath = "/static/post/files/photos";
//        String uploadPath = "/home/felici/studyPj/spring-boot-study/blog-api-namsan22/src/main/resources/static/post/files/";

        // 2. 원본 파일 이름 알아오기


        // 3. 파일 이름 중복되지 않게 이름 변경(서버에 저장할 이름) UUID 사용
//        UUID uuid = UUID.randomUUID();
        // 4. 파일 생성
//        File file1 = new File(uploadPath + File.separator + upLoadPhotoFileName);
//        File file1 = new File(uploadPath);
//        File file1 = new File(uploadPath);





        // 5. 서버로 전송
//        if(photoFile != null && !photoFile.exists()) {
//            photoFile.mkdirs();
//            photoMFile.transferTo(new File(uploadPath + "photo" + File.separator + upLoadPhotoFileName));
//        }
//        if(videoFile != null && !videoFile.exists()) {
//            videoFile.mkdirs();
//            videoFile.transferTo(new File(uploadPath +"video" + File.separator + upLoadPhotoFileName));
//        }



        String dpSavePathForPhoto = "post/files/photo";
        String dpSavePathForVideo = "post/files/video";

        String photoArg = null;
        String videoArg = null;

        if(upLoadPhotoFileName != "") {
            photoArg = dpSavePathForPhoto + File.separator + upLoadPhotoFileName;
        }
        if(upLoadVideoFileName != "") {
            videoArg = dpSavePathForPhoto + File.separator + upLoadVideoFileName;
        }



        letterRepository.save(Letter.builder()
                        .contents(addLetterDto.getContents())
                        .photo(photoArg)
                        .video(videoArg)
                        .build());

    }

    public Letter findById(Long letterId) {
        return letterRepository.findById(letterId)
                .orElseThrow();
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

    public void deleteLetter(Long id) {
        letterRepository.deleteById(id);
    }
}
