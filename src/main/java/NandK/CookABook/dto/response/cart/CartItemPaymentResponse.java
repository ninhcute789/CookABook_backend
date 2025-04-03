package NandK.CookABook.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemPaymentResponse {
    private Long id;
    private Integer quantity;
    private BookPaymentResponse book;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookPaymentResponse {
        private Long id;
        private String title;
        private String imageURL;
        private Integer originalPrice;
        private Integer finalPrice;
    }
}
