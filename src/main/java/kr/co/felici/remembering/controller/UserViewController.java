package kr.co.felici.remembering.controller;


import kr.co.felici.remembering.dto.AddUserDto;
import kr.co.felici.remembering.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(AddUserDto addUserDto) {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated AddUserDto addUserDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.info("에러가 있습니다. ");
            return "signup";
        }

        if((!addUserDto.getPassword().equals(addUserDto.getPassword2()))) {
            log.info("비번이 다르네요");
            bindingResult.rejectValue("password", "Incorrect", "비밀번호가 일치하지 않습니다. ");
            return "signup";
        }

        try {
            userService.save(addUserDto);
        } catch (DataIntegrityViolationException e) {
            log.warn("회원가입 실패: 이미 가입된 이메일");
            bindingResult.reject("signupFailure", "이미 존재하는 사용자입니다.");
            return "signup";
        }

        return "redirect:/login";
    }



}

