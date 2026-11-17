package kr.co.felici.remembering.dto;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class AlbumDto {

    private String title;
    private String info;
    private String descriptions;
    private String photo;
    private String video;
    private String isWhenPictureWasTakenClear;
    private String estimatedYear;
    private LocalDate tookIn;
    private int displayOrder;

}
