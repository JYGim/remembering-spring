package kr.co.felici.remembering.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * author: felici
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String contents;

    private String photo;

    private String video;

    private String originalPhotoName;

    @CreatedDate
    private LocalDateTime posted_at;

    private LocalDateTime modified_at;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @Builder
    public Letter(String contents, String photo, String video) {
        this.contents = contents;
        this.photo = photo;
        this.video = video;
    }
    //    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
//    contents = RichTextField('내용', config_name="title", blank=True)
//
//    image = models.ImageField(upload_to='letter/image',
//            null=True, blank=True,
//    verbose_name='첨부이미지', )
//    video = models.FileField('동영상', upload_to='letter/video', null=True, blank=True)
//    posted_at = models.DateTimeField('게시일시', auto_now_add=True)
//    modified_at = models.DateTimeField('수정일시', auto_now=True)


}
