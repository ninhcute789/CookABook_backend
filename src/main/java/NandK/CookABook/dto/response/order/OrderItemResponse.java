package NandK.CookABook.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private Long id;
    private Integer quantity;
    private Integer price;
    private Order order;
    private BookResponse book;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Order {
        private Long id;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookResponse {
        private Long bookId;
        private String bookName;
        private String bookImage;
        private Integer bookPrice;
    }
}
