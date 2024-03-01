package kr.co.felici.remembering.repository;



import kr.co.felici.remembering.domain.AlbumVideo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author: felici
 */
public interface VideoRepository extends JpaRepository<AlbumVideo, Long> {

}
