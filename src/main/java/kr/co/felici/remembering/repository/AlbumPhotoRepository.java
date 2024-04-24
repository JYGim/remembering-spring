package kr.co.felici.remembering.repository;



import kr.co.felici.remembering.domain.AlbumPhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * author: felici
 */
public interface AlbumPhotoRepository extends JpaRepository<AlbumPhoto, Long> {

    Page<AlbumPhoto> findAll(Pageable pageable);

}
