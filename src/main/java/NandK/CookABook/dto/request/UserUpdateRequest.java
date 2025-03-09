package NandK.CookABook.dto.request;

import java.time.LocalDate;

import NandK.CookABook.utils.constant.GenderEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @NotNull(message = "Id không được để trống")
    private Long id;

    private String password;
    private String name;
    private GenderEnum gender;
    private LocalDate dob;
    private String email;
}
