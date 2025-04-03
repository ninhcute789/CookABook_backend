package NandK.CookABook.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreationFromUserIdRequest {
    @NotNull(message = "Id người dùng không được để trống")
    private Long userId;
}
