package kr.co.felici.remembering.dto;


import kr.co.felici.remembering.domain.BoardImage;
import kr.co.felici.remembering.domain.MemorialPost;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemorialPostRequest {

    private String writer;
    private String contents;
    private String pw;
    private MultipartFile[] photo;
    private MultipartFile[] video;


    @Builder
    public MemorialPostRequest(String writer, String contents,
                                String pw, MultipartFile photos, MultipartFile videos) {

        this.writer = writer;
        this.contents = contents;
        this.pw = pw;


    }

    public MemorialPost toEntity() {
        return MemorialPost.builder()
                .writer(writer)
                .contents(contents)
                .pw(pw)
                .build();
    }

}

