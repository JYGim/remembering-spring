package kr.co.felici.remembering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddMemorialPostDto {

    @NotBlank
    @Size(min = 1, max = 64)
    private String writer;

    private String contents;

    private List<MultipartFile> photo;
    private List<MultipartFile> video;

    @NotBlank
    @Size(min = 3, max = 25)
    private String pw;

}
