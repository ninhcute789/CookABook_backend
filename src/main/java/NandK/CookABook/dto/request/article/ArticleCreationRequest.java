package NandK.CookABook.dto.request.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleCreationRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    private String content;
    private String imageURL;

    @NotNull(message = "Id người dùng không được để trống")
    private Long userId;
}
