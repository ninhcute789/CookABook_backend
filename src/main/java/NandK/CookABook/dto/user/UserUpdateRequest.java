package NandK.CookABook.dto.user;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private Long id;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String email;
}
