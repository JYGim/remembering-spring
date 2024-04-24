package kr.co.felici.remembering.dto;

/**
 * author: felici
 */
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddUserDto {

    @NotEmpty(message = "email을 입력해주세요!")
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String password2;

    @Size(min = 3, max = 25)
    @NotEmpty
    private String nickname;
}
