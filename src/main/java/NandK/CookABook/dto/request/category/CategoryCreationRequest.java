package NandK.CookABook.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreationRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;
}
