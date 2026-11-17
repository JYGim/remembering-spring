package kr.co.felici.remembering.dto;



import kr.co.felici.remembering.domain.Letter;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateLetterDto {

    private Long id;
    private String contents;
    private List<MultipartFile> photo;
    private List<MultipartFile> video;









}
