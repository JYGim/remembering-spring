package kr.co.felici.remembering.dto;

/**
 * author: felici
 */


import kr.co.felici.remembering.domain.MemorialPost;
import lombok.Getter;

@Getter
public class MemorialPostResponse {

    private Long id;
    private String writer;
    private String contents;
    private String photo;
    private String video;
    private String pw;

    public MemorialPostResponse(MemorialPost memorialPost) {
        this.writer = memorialPost.getWriter();
        this.contents = memorialPost.getContents();
        this.photo = memorialPost.getPhoto();
        this.video = memorialPost.getVideo();
        this.pw = memorialPost.getPw();
    }
}

