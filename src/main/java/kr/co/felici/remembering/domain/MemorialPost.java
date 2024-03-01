package kr.co.felici.remembering.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * author: felici
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemorialPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String writer;

    @Column(columnDefinition = "TEXT")
    private String contents;

    private String photo;

    private String video;

    @CreatedDate
    private LocalDateTime posted_at;

    private LocalDateTime modified_at;


    @Column(length = 26)
    private String pw;

    @Builder
    public MemorialPost(String writer, String contents, String photo, String video, String pw) {
        this.writer = writer;
        this.contents = contents;
        this.photo = photo;
        this.video = video;
        this.pw = pw;
    }



//    image = ImageWithThumbsField(upload_to=upload_to_board_file,
//                                 sizes=((200, 100),),
//
//    verbose_name='첨부이미지', null=True, blank=True)
//    video = models.FileField('동영상', upload_to=video_upload_to, null=True, blank=True)
//    posted_at = models.DateTimeField('게시일시', auto_now_add=True)
//    modified_at = models.DateTimeField('수정일시', auto_now=True)
//    pw = models.CharField('비밀번호', max_length=160)



//    posted_at = models.DateTimeField('게시일시', auto_now_add=True)
//    modified_at = models.DateTimeField('수정일시', auto_now=True)

}
