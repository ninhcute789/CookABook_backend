package NandK.CookABook.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordUpdateRequest {
    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;
}
