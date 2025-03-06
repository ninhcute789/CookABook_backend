package NandK.CookABook.dto.article;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleUpdateResponse {
    private Long id;
    private String title;
    private String content;
    private String imageURL;
    private String updatedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
}
