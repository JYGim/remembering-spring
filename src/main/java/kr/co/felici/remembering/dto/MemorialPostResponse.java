package kr.co.felici.remembering.dto;


import kr.co.felici.remembering.domain.BoardImage;
import kr.co.felici.remembering.domain.BoardVideo;
import kr.co.felici.remembering.domain.MemorialPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemorialPostResponse {

    private Long id;
    private String writer;
    private String contents;
    private String pw;
    private LocalDateTime postedAt;
    private LocalDateTime modifiedAt;
    private List<BoardImage> images;
    private List<BoardVideo> videos;

    public MemorialPostResponse(MemorialPost memorialPost) {
        this.id = memorialPost.getId();
        this.writer = memorialPost.getWriter();
        this.contents = memorialPost.getContents();
        this.pw = memorialPost.getPw();
        this.postedAt = memorialPost.getPostedAt();
        this.modifiedAt = memorialPost.getModifiedAt();
        this.images = memorialPost.getImages();
        this.videos = memorialPost.getVideos();
    }

    public void addImage(BoardImage image) {
        this.images.add(image);


    }

    public void addImages(List<BoardImage> images) {
        this.images = images;
    }








}

