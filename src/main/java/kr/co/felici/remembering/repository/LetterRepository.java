package kr.co.felici.remembering.repository;



import kr.co.felici.remembering.domain.AlbumPhoto;
import kr.co.felici.remembering.domain.Letter;
import kr.co.felici.remembering.domain.MemorialPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * author: felici
 */
public interface LetterRepository extends JpaRepository<Letter, Long> {

    Page<Letter> findAll(Specification<Letter> spec, Pageable pageable);
    Page<Letter> findAll(Pageable pageable);

    Page<Letter> findAllByUser(String email, Pageable pageable);


}
