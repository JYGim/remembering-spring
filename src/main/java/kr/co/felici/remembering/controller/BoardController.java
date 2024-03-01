package kr.co.felici.remembering.controller;


import jakarta.servlet.http.HttpServletRequest;

import kr.co.felici.remembering.domain.Letter;
import kr.co.felici.remembering.domain.MemorialPost;
import kr.co.felici.remembering.dto.AddLetterDto;
import kr.co.felici.remembering.dto.MemorialPostDto;
import kr.co.felici.remembering.dto.MemorialPostResponse;
import kr.co.felici.remembering.service.LetterService;
import kr.co.felici.remembering.service.MemorialPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * author: felici
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final MemorialPostService memorialPostService;
    private final LetterService letterService;

    @GetMapping("/letters")
    public String getAllLetters(Model model) {
        List<Letter> letters = letterService.getAllPost();
        model.addAttribute("letters", letters);
        return "board/letter_list";
    }

    @GetMapping("/letter/write")
    public String showLetterForm() {
        return "board/letter_form";
    }

    @PostMapping("/letter/write")
    public String addLetter(AddLetterDto addLetterDto) throws Exception {
        letterService.addLetter(addLetterDto);

        return "redirect:/board/letters";

    }

    @GetMapping("/letter/update/{letter_id}")
    public String updateLetterForm(@PathVariable(name = "letter_id") Long id,
                                   AddLetterDto addLetterDto, Model model) throws Exception {
        Letter theLetter = letterService.findById(id);
        model.addAttribute("letter", theLetter);
        return "board/letter_update_form";

    }

    @PostMapping("/letter/update")
    public String updateLetter(AddLetterDto addLetterDto) throws Exception {

        letterService.addLetter(addLetterDto);

        return "board/letter_form";

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

    @GetMapping("/posts")
    public String getAllPost(Model model, HttpServletRequest request) {
        List<MemorialPost> posts = memorialPostService.getAllPost();
        model.addAttribute("servletPath", request.getServletPath());
        model.addAttribute("posts", posts);

        return "board/memorialpost_list";
    }

    @GetMapping("/post/write")
    public String showPostForm() {
        return "board/memorialpost_form";
    }

    @PostMapping("/posts/write")
    public String addPost(MemorialPostDto memorialPostDto)
            throws Exception {
        memorialPostService.addPost(memorialPostDto);


        return "redirect:/board/posts";
    }

    @GetMapping("/post-detail/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        MemorialPost thePost = memorialPostService.findById(id);
        model.addAttribute("post", new MemorialPostResponse(thePost));

        return "/board/memorialpost_detail";
    }



}
