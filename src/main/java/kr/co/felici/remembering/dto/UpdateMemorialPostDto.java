package kr.co.felici.remembering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * author: felici
 */
@AllArgsConstructor
@Getter
@Builder
public class UpdateMemorialPostDto {

    private Long id;
    private String writer;
    private String contents;
    private List<MultipartFile> photo;
    private List<MultipartFile> video;

    @NotBlank
    @Size(min = 4, max = 25)
    private String pw;

//    private LocalDateTime posted_at;
//    private LocalDateTime modified_at;
//
//    private Member member;


}
