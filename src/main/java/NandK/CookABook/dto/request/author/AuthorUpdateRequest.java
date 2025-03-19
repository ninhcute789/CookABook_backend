package NandK.CookABook.dto.request.author;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorUpdateRequest {
    @NotNull(message = "Id tác giả không được để trống")
    private Long id;

    private String name;
}
