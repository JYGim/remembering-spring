package kr.co.felici.remembering.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * author: felici
 */
@Entity
@Getter
@ToString
@RequiredArgsConstructor
public class BoardImage implements BoardMultiMediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String path;

    private String originalFilename;

    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letter_id")
    @ToString.Exclude
    private Letter letter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memorialPost_id")
    @ToString.Exclude
    private MemorialPost memorialPost;

    @Builder
    public BoardImage(String path, String originalFilename, Long fileSize) {
        this.path = path;
        this.originalFilename = originalFilename;
        this.fileSize = fileSize;
    }

    public void setLetter(Letter letter) {
        this.letter = letter;
    }

    public void addMemorialPost(MemorialPost memorialPost) {
        this.memorialPost = memorialPost;
    }


}
