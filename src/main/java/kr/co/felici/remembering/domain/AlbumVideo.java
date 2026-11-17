package kr.co.felici.remembering.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@RequiredArgsConstructor
public class AlbumVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 126)
    private String title;

    @Column(length = 126)
    private String info;

    @Column(length = 512)
    private String descriptions;

    private String video;

    @Column(length = 2, nullable = false)
    private String isWhenPictureWasTakenclear;

    @Column(length = 4, nullable = true)
    private String estimatedYear;

    @Column(nullable = true)
    private LocalDate tookIn;

    @Column(nullable = true)
    private int displayOrder;

    @Builder
    public AlbumVideo(String title, String info, String descriptions,
                      String video, String isWhenPictureWasTakenclear,
                      String estimatedYear, LocalDate tookIn, int displayOrder) {
        this.title = title;
        this.info = info;
        this.descriptions = descriptions;
        this.video = video;
        this.isWhenPictureWasTakenclear = isWhenPictureWasTakenclear;
        this.estimatedYear = estimatedYear;
        this.tookIn = tookIn;
        this.displayOrder = displayOrder;
    }

}
