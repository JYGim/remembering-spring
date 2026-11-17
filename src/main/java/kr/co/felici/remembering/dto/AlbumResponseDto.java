package kr.co.felici.remembering.dto;

import kr.co.felici.remembering.domain.AlbumPhoto;
import kr.co.felici.remembering.domain.AlbumVideo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumResponseDto {

    private Long id;
    private String title;
    private String info;
    private String descriptions;
    private String photo;
    private String video;
    private String isWhenPictureWasTakenclear;
    private String estimatedYear;
    private LocalDate tookIn;
    private int displayOrder;

    public AlbumResponseDto(AlbumPhoto albumPhoto) {
        this.id = albumPhoto.getId();
        this.title = albumPhoto.getTitle();
        this.info = albumPhoto.getInfo();
        this.photo = albumPhoto.getPhoto();
        this.displayOrder = albumPhoto.getDisplayOrder();
        this.tookIn = albumPhoto.getTookIn();
    }

    public AlbumResponseDto(AlbumVideo albumVideo) {
        this.id = albumVideo.getId();
        this.title = albumVideo.getTitle();
        this.info = albumVideo.getInfo();
        this.photo = albumVideo.getVideo();
        this.displayOrder = albumVideo.getDisplayOrder();
        this.tookIn = albumVideo.getTookIn();
    }




}
