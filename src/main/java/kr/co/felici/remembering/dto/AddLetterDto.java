package kr.co.felici.remembering.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddLetterDto {

    private String email;

    @NotBlank(message = "내용을 기재해 주세요. ")
    private String contents;
    private List<MultipartFile> photo;
    private List<MultipartFile> video;









}
