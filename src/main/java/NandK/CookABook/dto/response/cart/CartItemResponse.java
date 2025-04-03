package NandK.CookABook.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private Long id;
    private Integer quantity;
    private Integer originalPrice;
    private Integer discountPrice;
    private Integer finalPrice;
    private Boolean selected;
    private BookResponse book;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookResponse {
        private Long id;
        private String title;
        private String imageURL;
        private Boolean official;
        private Integer originalPrice;
        private Double discountPercentage;
        private Integer finalPrice;
    }
}
