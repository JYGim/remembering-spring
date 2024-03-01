package kr.co.felici.remembering.dto;



import kr.co.felici.remembering.domain.Letter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * author: felici
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddLetterDto {

    private String contents;
    private MultipartFile photo;
    private MultipartFile video;

//    @Builder
//    public AddLetterDto(String contents, MultipartFile photo, MultipartFile video) {
//        this.contents = contents;
//        this.photo = photo;
//        this.video = video;
//    }

    public AddLetterDto(Letter letter) {
        this.contents = letter.getContents();
//        this.photo = letter.getPhoto();
//        this.video = letter.getVideo();
    }




    //    private LocalDateTime posted_at;
//    private LocalDateTime modified_at;
//



}
