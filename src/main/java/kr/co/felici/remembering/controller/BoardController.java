package kr.co.felici.remembering.controller;


import kr.co.felici.remembering.domain.Letter;
import kr.co.felici.remembering.domain.MemorialPost;
import kr.co.felici.remembering.domain.User;
import kr.co.felici.remembering.dto.AddLetterDto;
import kr.co.felici.remembering.dto.AddMemorialPostDto;
import kr.co.felici.remembering.dto.UpdateLetterDto;
import kr.co.felici.remembering.dto.UpdateMemorialPostDto;
import kr.co.felici.remembering.service.LetterService;
import kr.co.felici.remembering.service.MemorialPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final MemorialPostService memorialPostService;
    private final LetterService letterService;

    @GetMapping("/memorial-posts")
    public String getAllPost(Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "kw", defaultValue = "") String kw) {
        String keyword = kw.strip();
        Page<MemorialPost> posts = memorialPostService.getAll(page, keyword);

        model.addAttribute("posts", posts);
        model.addAttribute("kw", keyword);
        return "board/memorialpost_list";
    }

    @GetMapping("/memorial-post/write")
    public String showPostForm(AddMemorialPostDto addMemorialPostDto) {
        return "board/memorialpost_form";
    }

    @PostMapping("/memorial-post/write")
    public String addPost(@Validated AddMemorialPostDto addMemorialPostDto,
                          BindingResult bindingResult, Model model)
            throws Exception {

        if (bindingResult.hasErrors()) {
            log.error("bindingResult 에러가 있네");
            /* 회원가입 실패시 입력 데이터 값을 유지 */
            model.addAttribute("addMemorialPostDto", addMemorialPostDto);
            return "board/memorialpost_form";
        }

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
        model.addAttribute("id", thePost.getId());
        return "board/memorialpost_update_form";

    }

    @PostMapping("/memorial-post/update")
    public String updateMemorialPost(@Validated UpdateMemorialPostDto updateMemorialPostDto,
                                     BindingResult bindingResult,
                                     Model model,
                                     RedirectAttributes redirectAttributes) throws IOException {


        if (bindingResult.hasErrors()) {
            MemorialPost post = memorialPostService.findById(updateMemorialPostDto.getId());
            model.addAttribute("updateMemorialPostDto", updateMemorialPostDto);
            model.addAttribute("post", post);
            model.addAttribute("id", updateMemorialPostDto.getId());

            return "board/memorialpost_update_form";
        }
        boolean updated = memorialPostService.updateMemorialPost(updateMemorialPostDto);
        redirectAttributes.addFlashAttribute("updateIsSuccessful", updated);

        if (!updated) {
            MemorialPost post = memorialPostService.findById(updateMemorialPostDto.getId());
            model.addAttribute("updateMemorialPostDto", updateMemorialPostDto);
            model.addAttribute("post", post);
            model.addAttribute("id", updateMemorialPostDto.getId());
            return "board/memorialpost_update_form";
        }
        return "redirect:/board/memorial-posts";
    }


    @PostMapping("/memorial-post/deleteImg/")
    public String deletePostImage(@RequestParam Map<String, String> params,
                                  RedirectAttributes redirectAttributes) {

        boolean deleteIsSuccessful = memorialPostService.deletePostImage(params);
        redirectAttributes.addFlashAttribute("deleteIsSuccessful", deleteIsSuccessful);

        return "redirect:/board/memorial-post/update/" + params.get("postId");
    }

    @PostMapping("/memorial-post/deleteVideo/")
    public String deletePostVideo(@RequestParam Map<String, String> params,
                                  RedirectAttributes redirectAttributes) {

        boolean deleteIsSuccessful = memorialPostService.deletePostVideo(params);
        redirectAttributes.addFlashAttribute("deleteIsSuccessful", deleteIsSuccessful);

        return "redirect:/board/memorial-post/update/" + params.get("postId");
    }

    @PostMapping("/memorial-post/delete/")
    public String deleteMemorialPost(@RequestParam Map<String, String> params,
                                     RedirectAttributes redirectAttributes) {

        boolean deleted = memorialPostService.deleteMemorialPost(params);

        if (deleted) {
            redirectAttributes.addFlashAttribute("success", params.get("id"));
        } else {
            redirectAttributes.addFlashAttribute("deleteIsSuccessful", "-1");
        }

        return "redirect:/board/memorial-posts";
    }


    @GetMapping("/letters")
    public String getAllLetters(Model model,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "kw", defaultValue = "") String kw) {
        String keyword = kw.strip();
        Page<Letter> letters = letterService.getAll(page, keyword);

        model.addAttribute("paging", letters);
        model.addAttribute("kw", keyword);
        return "board/letter_list";
    }

    @GetMapping("/letter/write")
    public String showLetterForm(AddLetterDto addLetterDto) {
        return "board/letter_form";
    }

    @PostMapping("/letter/write")
    public String addLetter(@Validated AddLetterDto addLetterDto,
                            BindingResult bindingResult, Model model,
                            @AuthenticationPrincipal User user) throws Exception {

        if (bindingResult.hasErrors()) {
            model.addAttribute("addLetterDto", addLetterDto);
            return "board/letter_form";
        }

        addLetterDto.setEmail(user.getUsername());
        letterService.addLetter(addLetterDto);

        return "redirect:/board/letters";
    }

    @GetMapping("/letter/update/{letter_id}")
    public String updateLetterForm(@PathVariable(name = "letter_id") Long id,
                                   UpdateLetterDto updateLetterDto, Model model,
                                   @AuthenticationPrincipal User user) {
        Letter theLetter = letterService.findOwnedLetter(id, user);
        model.addAttribute("letter", theLetter);
        return "board/letter_update_form";
    }

    @PostMapping("/letter/update")
    public String updateLetter(UpdateLetterDto updateLetterDto,
                               @AuthenticationPrincipal User user) throws IOException {
        letterService.updateLetter(updateLetterDto, user);
        return "redirect:/board/letters";
    }

    @PostMapping("/letter/{letterId}/deleteImg/{imageId}")
    public String deleteLetterImage(@PathVariable Long letterId, @PathVariable Long imageId,
                                    @AuthenticationPrincipal User user) {
        letterService.deleteLetterImage(letterId, imageId, user);
        return "redirect:/board/letter/update/" + letterId;
    }

    @PostMapping("/letter/{letterId}/deleteVideo/{videoId}")
    public String deleteLetterVideo(@PathVariable Long letterId, @PathVariable Long videoId,
                                    @AuthenticationPrincipal User user) {
        letterService.deleteLetterVideo(letterId, videoId, user);
        return "redirect:/board/letter/update/" + letterId;
    }

    @PostMapping("/letter/delete/{id}")
    public String deleteLetter(@PathVariable Long id,
                               @AuthenticationPrincipal User user) {
        letterService.deleteLetter(id, user);
        return "redirect:/board/letters";
    }
}
