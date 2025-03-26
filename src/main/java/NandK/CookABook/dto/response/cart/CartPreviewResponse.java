package NandK.CookABook.dto.response.cart;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartPreviewResponse {
    private Long id;
    private Integer totalQuantity;
    private Integer totalOriginalPrice;
    private Integer totalDiscountPrice;
    private Integer totalFinalPrice;

    private List<CartItemResponse> cartItems;
}
