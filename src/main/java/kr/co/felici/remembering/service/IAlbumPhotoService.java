package kr.co.felici.remembering.service;





import kr.co.felici.remembering.domain.AlbumPhoto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAlbumPhotoService {


    Page<AlbumPhoto> getAll(int page);


}
