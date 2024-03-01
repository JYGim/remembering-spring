package kr.co.felici.remembering.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


/**
 * author: felici
 */
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
    private String is_WhenPictureWasTaken_clear;

    @Column(length = 4, nullable = true)
    private String estimated_year;

    @Column(nullable = true)
    private LocalDate took_in;

    @Column(nullable = true)
    private int display_order;



}
