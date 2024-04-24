package kr.co.felici.remembering.repository;



import kr.co.felici.remembering.domain.AlbumPhoto;
import kr.co.felici.remembering.domain.AlbumVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author: felici
 */
public interface AlbumVideoRepository extends JpaRepository<AlbumVideo, Long> {
    Page<AlbumVideo> findAll(Pageable pageable);
}
