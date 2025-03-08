package NandK.CookABook.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleUpdateRequest {

    @NotNull(message = "Id không được để trống")
    private Long id;

    private String title;
    private String content;
    private String imageURL;
}
