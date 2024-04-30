package kr.co.felici.remembering.service;



import kr.co.felici.remembering.domain.AlbumPhoto;
import kr.co.felici.remembering.repository.AlbumPhotoRepository;
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
public class AlbumPhotoServiceImpl implements IAlbumPhotoService {

    private final AlbumPhotoRepository albumPhotoRepository;


//    @Override
//    public void addPost(MemorialPostDto memorialPostForm) {
//
//    }

    public Page<AlbumPhoto> getAll(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("displayOrder"));
        PageRequest pageable = PageRequest.of(page, 4, Sort.by(sorts));
        return albumPhotoRepository.findAll(pageable);
    }

//    @Override
//    public Page<AlbumPhoto> getList(int page) {
//        return albumPhotoRepository.findAll();
//    }

    public Optional<AlbumPhoto> findById(Long imageId) {
        return albumPhotoRepository.findById(imageId);
    }



}
