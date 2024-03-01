package kr.co.felici.remembering.repository;



import kr.co.felici.remembering.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author: felici
 */
public interface LetterRepository extends JpaRepository<Letter, Long> {



}
