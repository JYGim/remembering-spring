package kr.co.felici.remembering.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import kr.co.felici.remembering.dto.MemorialPostRequest;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @JsonManagedReference
    private List<BoardImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "memorialPost", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<BoardVideo> videos = new ArrayList<>();

    @Column(length = 512, nullable = false)
    @NotEmpty(message = "비밀번호를 입력해 주세요. ")
    @NotBlank
    private String pw;


    @Builder
    public MemorialPost(String writer, String contents, String pw) {
        this.writer = writer;
        this.contents = contents;
        this.pw = pw;
    }

    @Builder
    public MemorialPost(String writer, String contents, List<BoardImage> images, List<BoardVideo> videos, String pw) {
        this.writer = writer;
        this.contents = contents;
        this.images = images;
        this.videos = videos;
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

    public void addImages(List<BoardImage> images) {
        this.images = images;
    }

    public void addVideos(List<BoardVideo> videos) {
        this.videos = videos;
    }

    public void addVideo(BoardVideo video) {
        this.videos.add(video);

        if(video.getMemorialPost() != this) {
            video.addMemorialPost(this);
        }
    }







}
