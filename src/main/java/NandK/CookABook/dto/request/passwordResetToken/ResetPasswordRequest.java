package NandK.CookABook.dto.request.passwordResetToken;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;
}
