package kr.co.felici.remembering.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.felici.remembering.domain.MemorialPost;
import kr.co.felici.remembering.service.MemorialPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * author: felici
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/testb/")
public class TestController {

    private final MemorialPostService memorialPostService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
