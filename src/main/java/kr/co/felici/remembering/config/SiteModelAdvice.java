package kr.co.felici.remembering.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 모든 화면에서 ${site...}로 사이트 설정을 쓸 수 있게 한다.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class SiteModelAdvice {

    private final SiteProperties siteProperties;

    @ModelAttribute("site")
    public SiteProperties site() {
        return siteProperties;
    }

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
