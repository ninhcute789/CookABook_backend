package NandK.CookABook.dto.response.cart;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartPaymentResponse {
    private Long id;
    private Integer totalQuantity;
    private Integer totalOriginalPrice;
    private Integer totalDiscountPrice;
    private Integer totalFinalPrice;

    private List<CartItemPaymentResponse> cartItems;
}
