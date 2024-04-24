package kr.co.felici.remembering.service;

import jakarta.persistence.EntityManager;
import kr.co.felici.remembering.domain.BoardVideo;
import kr.co.felici.remembering.repository.BoardVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * author: felici
 */
@Service
@RequiredArgsConstructor
public class BoardVideoService {

    private final BoardVideoRepository boardVideoRepository;
    private final EntityManager em;

    public BoardVideo findById(Long BoardVideoId) {
        return boardVideoRepository.findById(BoardVideoId)
                .orElseThrow();
    }

    @Transactional
    public void deleteBoardVideo(Long BoardVideoId) {
        BoardVideo video = em.find(BoardVideo.class, BoardVideoId);

        boardVideoRepository.delete(video);




    }


}
