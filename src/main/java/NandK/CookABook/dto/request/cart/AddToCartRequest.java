package NandK.CookABook.dto.request.cart;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequest {
    @NotNull(message = "Id giỏ hàng không được để trống")
    private Long cartId;

    @NotNull(message = "Id sách không được để trống")
    private Long bookId;

    @NotNull(message = "Số lượng không được để trống")
    private Integer quantity;
}
