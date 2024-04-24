package kr.co.felici.remembering.dto;



import kr.co.felici.remembering.domain.Letter;
import kr.co.felici.remembering.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * author: felici
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddLetterDto {

    private String email;
    private String contents;
    private List<MultipartFile> photo;
    private List<MultipartFile> video;

//    @Builder
//    public AddLetterDto(String contents, MultipartFile photo, MultipartFile video) {
//        this.contents = contents;
//        this.photo = photo;
//        this.video = video;
//    }

//    public AddLetterDto(Letter letter) {
//        this.contents = letter.getContents();
//        this.photo = letter.getPhoto();
//        this.video = letter.getVideo();
//    }




    //    private LocalDateTime posted_at;
//    private LocalDateTime modified_at;
//



}
