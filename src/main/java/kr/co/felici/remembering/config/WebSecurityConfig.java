package kr.co.felici.remembering.config;

/**
 * author: felici
 */


import kr.co.felici.remembering.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final UserDetailService userDetailService;
    private final CustomAuthDetails customAuthDetails;



//    protected void configure(AuthenticationManagerBuilder authentication) {
//        authentication
//                .inMemoryAuthentication()
//                .withUser(
//                        User.withDefaultPasswordEncoder()
//                                .username("user1")
//                                .
//                )
//    }

    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations());
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                        .requestMatchers(
                                "/memorialPosts/images/**", "/letters/images/**",
                                "/favicon.ico", "/error",
                                "/", "/login", "/signup", "/user",
                                "/album/**",
                                "/board/excercise/**",
                                "/board/memorial-posts/**",
                                "/board/memorial-post/**",
                                "/board/memorial-post/update/**",
                                "/board/memorial-post/delete/**",
                                "/memorial-post/deleteVideo/**",
                                "/testb/memorial-posts/**",
                                "/testb/test", "/mycss/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin()
                        .loginPage("/login")
                        .defaultSuccessUrl("/", false)
//                        .failureUrl("/login-error")
                        .authenticationDetailsSource(customAuthDetails)
                        .and()
                        .logout()
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .and()
                        .csrf().disable()
                        .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

