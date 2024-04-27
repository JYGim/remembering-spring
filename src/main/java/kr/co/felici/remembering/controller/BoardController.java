package kr.co.felici.remembering.controller;


import kr.co.felici.remembering.domain.*;

import kr.co.felici.remembering.dto.AddLetterDto;
import kr.co.felici.remembering.dto.AddMemorialPostDto;
import kr.co.felici.remembering.dto.UpdateLetterDto;
import kr.co.felici.remembering.dto.UpdateMemorialPostDto;
import kr.co.felici.remembering.service.BoardVideoService;
import kr.co.felici.remembering.service.BoardImageService;
import kr.co.felici.remembering.service.LetterService;
import kr.co.felici.remembering.service.MemorialPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: felici
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final MemorialPostService memorialPostService;
    private final LetterService letterService;
    private final BoardImageService imageService;
    private final BoardVideoService boardVideoService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


//    String absolutePath = new File("").getAbsolutePath() + File.separator;
//    String uploadRootPath = absolutePath + "media";
    String basePath = "/home/felici/studyPj/spring-boot-study/remembering/media";
    String lettersFilePath = basePath + "/letters";
    String memorialPostsFilePath = basePath + "/memorialPosts";

    @GetMapping("/letters")
    public String getAllLetters(Model model,
                                @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Letter> paging = letterService.getAll(page);
//        List<BoardImage> letterImages = new ArrayList<>();
//
//        List<BoardImage> allImages = imageService.findAllImages();
//        allImages.stream().forEach(image -> {
//            if(!(image.getLetter() == null)) {
//                letterImages.add(image);
//            }
//        });


        model.addAttribute("paging", paging);
//        model.addAttribute("images", allImages);
        
        return "board/letter_list";
    }

    @GetMapping("/letter/write")
    public String showLetterForm() {
//        model.addAttribute("user", (User)principal);
        return "board/letter_form";
    }

    @PostMapping("/letter/write")
    public String addLetter(AddLetterDto addLetterDto) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();

        log.info("Controller - username: " , username);

        addLetterDto.setEmail(username);





        letterService.addLetter(addLetterDto);

        return "redirect:/board/letters";
    }

    @GetMapping("/letter/update/{letter_id}")
    public String updateLetterForm(@PathVariable(name = "letter_id") Long id,
                                   UpdateLetterDto updateLetterDto, Model model) {
        Letter theLetter = letterService.findById(id);
        model.addAttribute("letter", theLetter);
        return "board/letter_update_form";
    }

    @PostMapping("/letter/update")
    public String updateLetter(UpdateLetterDto updateLetterDto,
                               Model model) throws IOException {
        Letter theLetter = letterService.updateLetter(updateLetterDto);
        model.addAttribute("letter", theLetter);

        return "redirect:/board/letters";
    }

    @GetMapping("/letter/deleteImg/{image_id}")
    public String deleteImage(@PathVariable(name = "image_id") Long image_id,
                                   Model model) throws Exception {
        BoardImage image = imageService.findById(image_id);
        Letter letter = image.getLetter();
        imageService.deleteImage(image_id);

        deleteFiles(image, null, lettersFilePath);

        model.addAttribute("letter", letter);
        return "redirect:/board/letter/update/" + letter.getId();
    }

    @GetMapping("/letter/deleteVideo/{video_id}")
    public String deleteVideo(@PathVariable(name = "video_id") Long video_id,
                              Model model) throws Exception {
        BoardVideo video = boardVideoService.findById(video_id);
        Letter letter = video.getLetter();
        boardVideoService.deleteBoardVideo(video_id);

        deleteFiles(null, video, lettersFilePath);

        model.addAttribute("letter", letter);
        return "redirect:/board/letter/update/" + letter.getId();
    }

    @GetMapping("/letter/view/{id}")
    public String viewLetter(@PathVariable Long id, Model model) {
        Letter theLetter = letterService.findById(id);
        model.addAttribute("letter", theLetter);
        return "/board/letter_detail";
    }

    @GetMapping("/letter/delete/{id}")
    public String deleteLetter(@PathVariable Long id) {

        letterService.deleteLetter(id);
        return "redirect:/board/letters";
    }

    @GetMapping("/excercise")
    public String ex(Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<MemorialPost> posts = memorialPostService.getAll(page);

        model.addAttribute("posts", posts);

        return "board/excercise";
    }

    @GetMapping("/memorial-posts")
    public String getAllPost(Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<MemorialPost> posts = memorialPostService.getAll(page);

        model.addAttribute("posts", posts);

        return "board/memorialpost_list";
    }

    @GetMapping("/memorial-post/write")
    public String showPostForm(AddMemorialPostDto addMemorialPostDto) {
        return "board/memorialpost_form";
    }

    @PostMapping("/memorial-post/write")
    public String addPost(AddMemorialPostDto addMemorialPostDto)
            throws Exception {
        memorialPostService.addPost(addMemorialPostDto);
        return "redirect:/board/memorial-posts";
    }

    @GetMapping("/memorial-post/update/{memorialPostId}")
    public String updateMemorialPostForm(@PathVariable(name = "memorialPostId") Long id,
                                         UpdateMemorialPostDto updateMemorialPostDto,
                                         Model model) {
        memorialPostService.checkAuth(id);


        MemorialPost thePost = memorialPostService.findById(id);
        model.addAttribute("post", thePost);
        return "board/memorialpost_update_form";

    }

    @PostMapping("/memorial-post/update")
    public String updateMemorialPost(UpdateMemorialPostDto updateMemorialPostDto,
                                        RedirectAttributes redirectAttributes) throws IOException {
        MemorialPost thePost = memorialPostService.updateMemorialPost(updateMemorialPostDto);

        Boolean isSuccessful = memorialPostService.succeed();
        redirectAttributes.addFlashAttribute("isSuccessful", isSuccessful);
        log.info("isSuccessful: " + isSuccessful);
        return "redirect:/board/memorial-posts";
    }

    @GetMapping("/memorial-post/deleteImg/{image_id}")
    public String deletePostImage(@PathVariable(name = "image_id") Long image_id,
                                  Model model) throws Exception {
        BoardImage image = imageService.findById(image_id);
        MemorialPost memorialPost = image.getMemorialPost();
        imageService.deleteImage(image_id);

        deleteFiles(image, null, memorialPostsFilePath);

        model.addAttribute("memorialPost", memorialPost);
        return "redirect:/board/memorial-post/update/" + memorialPost.getId();
    }

    @PostMapping("/memorial-post/deleteImg/")
    public String deletePostImage(@RequestParam Map<String, String> params,
                                  RedirectAttributes redirectAttributes
                                  ) throws Exception {

        MemorialPost memorialPost = memorialPostService.deletePostImage(params);

        String imagePathToDelete = memorialPostService.passDeletedImagePath();
        Boolean isSuccessFul = memorialPostService.succeed();
        log.info("log, imagePathTodelete: " + imagePathToDelete);
        log.info("log, isSuccessFul: " + isSuccessFul);

        if(imagePathToDelete != "") {

            deleteFile(imagePathToDelete, "image", memorialPostsFilePath);


        }


        redirectAttributes.addFlashAttribute("isSuccessFul", isSuccessFul);

        return "redirect:/board/memorial-post/update/" + memorialPost.getId();
    }

    @GetMapping("/memorial-post/deleteVideo/{video_id}")
    public String deletePostsVideo(@PathVariable(name = "video_id") Long video_id,
                                   Model model) throws Exception {
        BoardVideo video = boardVideoService.findById(video_id);
        MemorialPost memorialPost = video.getMemorialPost();
        boardVideoService.deleteBoardVideo(video_id);

        deleteFiles(null, video, memorialPostsFilePath);

        model.addAttribute("memorialPost", memorialPost);
        return "redirect:/board/memorial-post/update/" + memorialPost.getId();

    }

    @GetMapping("/memorial-post/view/{id}")
    public String viewMemorialPost(@PathVariable Long id, Model model) {
        MemorialPost theMemorialPost = memorialPostService.findById(id);
        String rootPath = "/home/felici/studyPj/spring-boot-study/remembering/media/memorialPosts/";
        model.addAttribute("post", theMemorialPost);
        model.addAttribute("rootPath", rootPath);
        return "/board/memorialpost_detail";
    }

//    @GetMapping("/memorial-post/delete/{id}")
//    public String deleteMemorialPost(@PathVariable Long id) {
//
//        memorialPostService.deleteMemorialPost(id);
//        return "redirect:/board/memorial-posts";
//    }

    @PostMapping("/memorial-post/delete/")
    public String deleteMemorialPost(@RequestParam Map<String, String> params,
                                     RedirectAttributes redirectAttributes) {

        String resultStr = memorialPostService.deleteMemorialPost(params);

        if(resultStr == params.get("id")) {
            log.info("resultStr: " + resultStr);
            redirectAttributes.addFlashAttribute("success", resultStr);
        } else {
            log.info("resultStr: " + resultStr);
            redirectAttributes.addFlashAttribute("error", resultStr);
        }

        return "redirect:/board/memorial-posts";
    }

//    @GetMapping("/memorial-post/search/{q}")
//    public List<MemorialPost> searchMemorialPost(@RequestParam("searchString") String q) {
//
//        List<MemorialPost> memorialPosts = memorialPostService.searchMemorialPost(searchString);
//
//
//        return memorialPosts;
//    }


    public void deleteFiles(BoardImage image, BoardVideo video, String type) {
        if(image != null) {
            try {
                File file = new File( type + File.separator + "images" + File.separator + image.getPath());

                if(file.exists()) {
                    boolean result = file.delete();
                    if(result) {
                        log.info("파일 지웠어요.");
                    }
                } else {
                    log.info("파일 없어요.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(video != null) {
            try {
                File file = new File( type + File.separator + "videos" + File.separator + video.getPath());

                if(file.exists()) {
                    boolean result = file.delete();
                    if(result) {
                        System.out.println("파일 지웠어요.");
                    }
                } else {
                    System.out.println("파일 없어요.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFile(String path, String mediaType, String type) {
        if(mediaType.equals("image")) {
            try {
                File file = new File( type + File.separator + "images" + File.separator + path);

                if(file.exists()) {
                    boolean result = file.delete();
                    if(result) {
                        log.info("파일 지웠어요.");
                    }
                } else {
                    log.info("파일 없어요.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(mediaType.equals("video")) {
            try {
                File file = new File( type + File.separator + "videos" + File.separator + path);

                if(file.exists()) {
                    boolean result = file.delete();
                    if(result) {
                        System.out.println("파일 지웠어요.");
                    }
                } else {
                    System.out.println("파일 없어요.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }




    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.isAuthenticated();

    }





}
