package kr.co.felici.remembering.repository;



import kr.co.felici.remembering.domain.AlbumPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author: felici
 */
public interface PhotoRepository extends JpaRepository<AlbumPhoto, Long> {

}
