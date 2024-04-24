package kr.co.felici.remembering.dto;



import kr.co.felici.remembering.domain.Letter;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * author: felici
 */

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

//    @Builder
//    public AddLetterDto(String contents, MultipartFile photo, MultipartFile video) {
//        this.contents = contents;
//        this.photo = photo;
//        this.video = video;
//    }

//    public UpdateLetterDto(Letter letter) {
//
//        this.contents = letter.getContents();
////        this.photo = letter.getPhoto();
////        this.video = letter.getVideo();
//    }




    //    private LocalDateTime posted_at;
//    private LocalDateTime modified_at;
//



}
