package NandK.CookABook.dto.article;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleCreationRequest {

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    private String content;
    private String imageURL;
    private Instant createdAt;
    private String createdBy;
}
