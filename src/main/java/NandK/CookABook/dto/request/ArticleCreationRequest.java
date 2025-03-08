package NandK.CookABook.dto.request;

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
}
