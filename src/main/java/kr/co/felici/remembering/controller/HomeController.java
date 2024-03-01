package kr.co.felici.remembering.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * author: felici
 */
@Controller
@RequestMapping(value = "/", method = RequestMethod.GET)
public class HomeController {

    @GetMapping
    public String homePage() {
        return "home";
    }







}
