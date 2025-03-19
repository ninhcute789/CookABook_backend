package NandK.CookABook.dto.request.author;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreationRequest {
    @NotBlank(message = "Tên tác giả không được để trống")
    private String name;
}
