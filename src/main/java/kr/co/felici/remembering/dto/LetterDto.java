package kr.co.felici.remembering.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * author: felici
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LetterDto {

    private Long id;
    private String email;
    private String contents;
    private List<MultipartFile> photo;
    private List<MultipartFile> video;


}
