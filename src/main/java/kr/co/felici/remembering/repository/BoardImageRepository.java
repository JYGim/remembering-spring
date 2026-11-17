package kr.co.felici.remembering.repository;

import kr.co.felici.remembering.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    BoardImage findByPath(String path);

}
