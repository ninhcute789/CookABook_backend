package NandK.CookABook.dto.user;

import java.time.LocalDate;

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
    private String firstName;
    private String lastName;
    private LocalDate dob;
    @NotBlank(message = "Email không được để trống")
    private String email;
}
