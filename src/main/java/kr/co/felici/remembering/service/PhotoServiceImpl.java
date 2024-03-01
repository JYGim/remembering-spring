package kr.co.felici.remembering.service;



import kr.co.felici.remembering.domain.AlbumPhoto;
import kr.co.felici.remembering.repository.PhotoRepository;
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
public class PhotoServiceImpl implements IPhotoService {

    private final PhotoRepository photoRepository;


//    @Override
//    public void addPost(MemorialPostDto memorialPostForm) {
//
//    }

    public List<AlbumPhoto> getAll() {
        return photoRepository.findAll();
    }

    public Optional<AlbumPhoto> findById(Long imageId) {
        return photoRepository.findById(imageId);
    }



}
