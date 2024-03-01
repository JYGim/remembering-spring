package kr.co.felici.remembering.service;



import kr.co.felici.remembering.domain.AlbumVideo;
import kr.co.felici.remembering.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * author: felici
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VideoServiceImpl implements IVideoService {

    private final VideoRepository videoRepository;





    public List<AlbumVideo> getAll() {
        return videoRepository.findAll();
    }

    public Optional<AlbumVideo> findById(Long imageId) {
        return videoRepository.findById(imageId);
    }

}
