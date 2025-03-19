package NandK.CookABook.dto.response.article;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleCreationResponse {
    private Long id;
    private String title;
    private String content;
    // private String imageId;
    private String imageURL;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;

    private User user;

    @Getter
    @Setter
    public static class User {
        private Long id;
        private String name;
    }
}
