package kr.co.felici.remembering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * author: felici
 */
@AllArgsConstructor
@Data
public class MemorialPostDto {

    private String writer;
    private String contents;
    private MultipartFile photo;
    private MultipartFile video;
    private String pw;

//    private LocalDateTime posted_at;
//    private LocalDateTime modified_at;
//
//    private Member member;


}
