package kr.co.felici.remembering.service;



import kr.co.felici.remembering.domain.AlbumPhoto;
import kr.co.felici.remembering.domain.AlbumVideo;
import kr.co.felici.remembering.repository.AlbumVideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * author: felici
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumVideoServiceImpl implements IAlbumVideoService {

    private final AlbumVideoRepository videoRepository;

    public Page<AlbumVideo> getAll(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("displayOrder"));
        PageRequest pageable = PageRequest.of(page, 2, Sort.by(sorts));
        return videoRepository.findAll(pageable);
    }

    public Optional<AlbumVideo> findById(Long imageId) {
        return videoRepository.findById(imageId);
    }


}
