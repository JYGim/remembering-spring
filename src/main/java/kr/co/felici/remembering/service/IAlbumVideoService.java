package kr.co.felici.remembering.service;





import kr.co.felici.remembering.dto.AlbumResponseDto;
import kr.co.felici.remembering.domain.AlbumVideo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAlbumVideoService {
    Page<AlbumVideo> getAll(int page);
    List<AlbumResponseDto> findAllByOrderByDisplayOrderDesc();

}
