package NandK.CookABook.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemPreviewResponse {
    private Long id;
    private Integer quantity;
    private Integer price;
    private BookResponse book;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookResponse {
        private Long bookId;
        private String bookTitle;
        private String bookImageURL;
        private Integer bookFinalPrice;
    }
}
