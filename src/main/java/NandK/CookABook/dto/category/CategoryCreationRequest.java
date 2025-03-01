package NandK.CookABook.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreationRequest {

    @NotBlank(message = "Tên thể loại không được để trống")
    private String name;

}
