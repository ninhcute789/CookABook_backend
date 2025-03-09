package NandK.CookABook.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String token;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String newPassword;
}
