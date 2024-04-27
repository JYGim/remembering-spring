package kr.co.felici.remembering.repository;



import kr.co.felici.remembering.domain.Letter;
import kr.co.felici.remembering.domain.MemorialPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * author: felici
 */
public interface MemorialPostRepository extends JpaRepository<MemorialPost, Long> {


//    List<MemorialPost> findByWriterOrContents(String searchString);
//    Page<List<Letter>> findByWriterOrContents(Pageable<List<String searchString> > pageable);
}
