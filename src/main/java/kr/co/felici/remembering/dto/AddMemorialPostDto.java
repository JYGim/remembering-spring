package kr.co.felici.remembering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * author: felici
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddMemorialPostDto {

    @NotBlank
//    @NotEmpty(message = "닉네임이나 이름을 입력해주세요.")
    @Size(min = 1, max = 64)
    private String writer;

    private String contents;

    private List<MultipartFile> photo;
    private List<MultipartFile> video;

    @NotBlank
//    @NotEmpty(message = "비밀번호 입력은 필수입니다. ")
    @Size(min = 3, max = 25)
    private String pw;

}
