package kr.co.felici.remembering.repository;



import kr.co.felici.remembering.domain.AlbumPhoto;
import kr.co.felici.remembering.domain.Letter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author: felici
 */
public interface LetterRepository extends JpaRepository<Letter, Long> {

    Page<Letter> findAll(Pageable pageable);

}
