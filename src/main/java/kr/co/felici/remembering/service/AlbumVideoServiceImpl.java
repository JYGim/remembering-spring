package kr.co.felici.remembering.service;



import kr.co.felici.remembering.dto.AlbumResponseDto;
import kr.co.felici.remembering.domain.AlbumVideo;
import kr.co.felici.remembering.repository.AlbumVideoRepository;
import kr.co.felici.remembering.util.AlbumVideoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumVideoServiceImpl implements IAlbumVideoService {

    private final AlbumVideoRepository videoRepository;
    private final AlbumVideoMapper albumVideoMapper;

    public Page<AlbumVideo> getAll(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("displayOrder"));
        PageRequest pageable = PageRequest.of(page, 2, Sort.by(sorts));
        return videoRepository.findAll(pageable);
    }

    public Optional<AlbumVideo> findById(Long imageId) {
        return videoRepository.findById(imageId);
    }

    @Override
    public List<AlbumResponseDto> findAllByOrderByDisplayOrderDesc() {
        return videoRepository.findAllByOrderByDisplayOrderDesc()
                .stream().map(AlbumResponseDto::new).toList();
    }



}
