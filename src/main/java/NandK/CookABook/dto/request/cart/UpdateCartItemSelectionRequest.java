package NandK.CookABook.dto.request.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemSelectionRequest {
    @NotNull(message = "Id sản phẩm trong giỏ hàng không được để trống")
    private Long cartItemId;

    @NotNull(message = "Trạng thái sản phẩm không được để trống")
    private Boolean selected;
}
