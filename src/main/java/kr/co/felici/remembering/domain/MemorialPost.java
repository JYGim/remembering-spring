package kr.co.felici.remembering.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * author: felici
 */
@Entity
@Getter
@NoArgsConstructor
public class MemorialPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String writer;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @OneToMany(mappedBy = "memorialPost", cascade = CascadeType.REMOVE)
    private List<BoardImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "memorialPost", cascade = CascadeType.REMOVE)
    private List<BoardVideo> videos = new ArrayList<>();

    @Column(length = 126, nullable = false)
    private String pw;

    @Builder
    public MemorialPost(String writer, String contents, String pw) {
        this.writer = writer;
        this.contents = contents;
        this.pw = pw;
    }

    @Builder
    public MemorialPost(String writer, String contents, BoardImage image, BoardVideo video, String pw) {
        this.writer = writer;
        this.contents = contents;
        this.images.add(image);
        this.videos.add(video);
        this.pw = pw;
    }



    public void addContents(String contents) {
        this.contents = contents;
    }

    public void addImage(BoardImage image) {
        this.images.add(image);

        if(image.getMemorialPost() != this) {
            image.addMemorialPost(this);
        }
    }

    public void addVideo(BoardVideo video) {
        this.videos.add(video);

        if(video.getMemorialPost() != this) {
            video.addMemorialPost(this);
        }
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
