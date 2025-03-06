package NandK.CookABook.dto.user;

import java.time.LocalDate;

import NandK.CookABook.utils.constant.GenderEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationRequest {
    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Password không được để trống")
    private String password;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Giới tính không được để trống")
    private GenderEnum gender;

    @NotBlank(message = "Ngày sinh không được để trống")
    private LocalDate dob;

    @NotBlank(message = "Email không được để trống")
    private String email;
}
