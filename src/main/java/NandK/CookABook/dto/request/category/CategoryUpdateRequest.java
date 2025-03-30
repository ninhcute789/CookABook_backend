package NandK.CookABook.dto.request.category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateRequest {
    @NotNull(message = "Id danh mục không được để trống")
    private Long id;

    private String name;
}
