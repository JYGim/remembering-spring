package kr.co.felici.remembering.service;





import kr.co.felici.remembering.domain.AlbumVideo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * author: felici
 */
public interface IAlbumVideoService {
    Page<AlbumVideo> getAll(int page);

}
