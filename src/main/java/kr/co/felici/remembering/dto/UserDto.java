package kr.co.felici.remembering.dto;

/**
 * author: felici
 */
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserDto {

    private String email;

    private String password;

    private String nickname;
}
