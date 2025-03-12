package NandK.CookABook.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorCreationRequest {
    @NotBlank(message = "Tên tác giả không được để trống")
    private String name;
}
