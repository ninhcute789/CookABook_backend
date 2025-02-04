package NandK.CookABook.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserCreationRequest {
    
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String email;
}
