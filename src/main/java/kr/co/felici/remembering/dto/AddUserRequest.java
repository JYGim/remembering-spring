package kr.co.felici.remembering.dto;

/**
 * author: felici
 */
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddUserRequest {
    private String email;
    private String password;
    private String password2;
    private String nickname;
}
