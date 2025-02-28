package NandK.CookABook.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResLogin {
    private String accessToken;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
