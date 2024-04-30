package kr.co.felici.remembering.dto;

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

    private String writer;

    private String contents;

    private List<MultipartFile> photo;
    private List<MultipartFile> video;
    private String pw;

}
