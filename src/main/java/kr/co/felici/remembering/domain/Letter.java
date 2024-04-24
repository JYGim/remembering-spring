package kr.co.felici.remembering.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * author: felici
 */
@Entity
@Getter
@NoArgsConstructor
public class Letter extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @OneToMany(mappedBy = "letter", cascade = CascadeType.REMOVE)
    private List<BoardImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "letter", cascade = CascadeType.REMOVE)
    private List<BoardVideo> videos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Letter(String contents, User user) {
        this.contents = contents;
        this.user = user;
//        this.images.add(images;
    }

    public void addContents(String contents) {
        this.contents = contents;
    }

    public void addImage(BoardImage image) {
        this.images.add(image);

        if(image.getLetter() != this) {
            image.setLetter(this);
        }
    }

    public void addImages(List<BoardImage> images) {
        this.images.addAll(images);

        for(BoardImage image: images) {
            if(image.getLetter() != this) {
                image.setLetter(this);
            }
        }
    }

    public void addVideo(BoardVideo video) {
        this.videos.add(video);

        if(video.getLetter() != this) {
            video.setLetter(this);
        }
    }

    public void addVideos(List<BoardVideo> videos) {
        this.videos.addAll(videos);

        for(BoardVideo video: videos) {
            if(video.getLetter() != this) {
                video.setLetter(this);
            }
        }
    }

    public void addUser(User user) {
        this.user = user;


    }

    @Override
    public String toString() {
        return "Letter{" +
                "user='" + user + '\'' +
                '}';
    }


}
