package kr.co.felici.remembering.repository;

import kr.co.felici.remembering.domain.BoardVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardVideoRepository extends JpaRepository<BoardVideo, Long> {
}
