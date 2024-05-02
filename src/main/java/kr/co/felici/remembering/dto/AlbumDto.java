package kr.co.felici.remembering.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * author: felici
 */
@Setter
@Getter
public class AlbumDto {

    private String title;

    private String info;

    private String descriptions;

    private String photo;

    private String is_WhenPictureWasTaken_clear;

    private String estimated_year;

    private LocalDate took_in;

    private int displayOrder;


}
