package NandK.CookABook.dto.request.passwordResetToken;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {
    @NotBlank(message = "Email không được để trống")
    private String email;
}
