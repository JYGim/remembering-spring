package kr.co.felici.remembering.controller;



import kr.co.felici.remembering.domain.AlbumPhoto;
import kr.co.felici.remembering.domain.AlbumVideo;
import kr.co.felici.remembering.service.PhotoServiceImpl;
import kr.co.felici.remembering.service.VideoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * author: felici
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/album")
public class AlbumController {

    private final PhotoServiceImpl photoService;
    private final VideoServiceImpl videoService;

    @GetMapping("/photos")
    public String retrieveAllPhotos(Model model){
        List<AlbumPhoto> photoList = photoService.getAll();
        model.addAttribute("photoList", photoList);
        return "photo_album";
    }

    @GetMapping("/video")
    public String retrieveAllVideos(Model model){
        List<AlbumVideo> videoList = videoService.getAll();
        model.addAttribute("videoList", videoList);
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








}
