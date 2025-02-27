package NandK.CookABook.dto.response;

import lombok.Data;

@Data
public class ResLogin {
    private String accessToken;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
