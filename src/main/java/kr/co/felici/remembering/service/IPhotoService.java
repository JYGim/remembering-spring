package kr.co.felici.remembering.service;





import kr.co.felici.remembering.domain.AlbumPhoto;

import java.util.List;

/**
 * author: felici
 */
public interface IPhotoService {

//    void addPost(MemorialPostDto memorialPostForm);

    List<AlbumPhoto> getAll();


}
