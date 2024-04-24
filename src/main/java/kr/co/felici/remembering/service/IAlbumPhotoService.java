package kr.co.felici.remembering.service;





import kr.co.felici.remembering.domain.AlbumPhoto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * author: felici
 */
public interface IAlbumPhotoService {

//    void addPost(MemorialPostDto memorialPostForm);

    Page<AlbumPhoto> getAll(int page);


}
