package kr.co.felici.remembering.dto;

/**
 * author: felici
 */


import kr.co.felici.remembering.domain.MemorialPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemorialPostResponse {

    private Long id;
    private String writer;
    private String contents;
    private List<MultipartFile> photo;
    private List<MultipartFile> video;
    private String pw;


    public MemorialPostResponse(MemorialPost memorialPost) {
        this.writer = memorialPost.getWriter();
        this.contents = memorialPost.getContents();
//        this.photo = memorialPost.getImages();
//        this.video = memorialPost.getVideo();
        this.pw = memorialPost.getPw();
    }
}

