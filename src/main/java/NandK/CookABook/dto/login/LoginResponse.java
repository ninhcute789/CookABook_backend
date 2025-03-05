package NandK.CookABook.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String accessToken;
    private UserLoginInformation user;

    // nested class
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLoginInformation {
        private Long id;
        private String username;
        private String name;
    }
}
