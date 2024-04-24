package kr.co.felici.remembering.controller;



import kr.co.felici.remembering.domain.AlbumPhoto;
import kr.co.felici.remembering.domain.AlbumVideo;
import kr.co.felici.remembering.repository.AlbumPhotoRepository;
import kr.co.felici.remembering.repository.AlbumVideoRepository;
import kr.co.felici.remembering.service.AlbumPhotoServiceImpl;
import kr.co.felici.remembering.service.AlbumVideoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * author: felici
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/album")
public class AlbumController {

    private final AlbumPhotoServiceImpl photoService;
    private final AlbumVideoServiceImpl videoService;
    private final AlbumPhotoRepository albumPhotoRepository;
    private final AlbumVideoRepository albumVideoRepository;

    @GetMapping("/photos")
    public String retrieveAllPhotos(Model model,
                                    @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<AlbumPhoto> paging = photoService.getAll(page);
        model.addAttribute("paging", paging);
        return "photo_album";
    }

    @GetMapping("/video")
    public String retrieveAllVideos(Model model,
                                    @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<AlbumVideo> paging = videoService.getAll(page);
        model.addAttribute("paging", paging);
        return "video_album";
    }

    @GetMapping("/photos/add")
    public ModelAndView addPhoto() {

        return new ModelAndView("addPhoto", "photo", new AlbumPhoto());
    }

//    @PostMapping(value = "/photos/add", consumes = MULTIPART_FORM_DATA_VALUE)
//    public String handleProfileAdd(AlbumPhoto photo, @RequestPart("file") MultipartFile file) {
//
//        log.info("handling request parts: {}", file);
//
//        return "redirect:/photos/view";
//    }

    public Page<AlbumPhoto> getAlbumPhotoPageList(int page) {
        PageRequest pageable = PageRequest.of(page, 20);
        return albumPhotoRepository.findAll(pageable);
    }








}
