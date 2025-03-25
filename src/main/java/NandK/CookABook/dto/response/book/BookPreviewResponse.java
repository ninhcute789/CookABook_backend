package NandK.CookABook.dto.response.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookPreviewResponse {
    private Long id;
    private String title;
    private String author;
    private String imageURL;
    private Integer originalPrice;
    private Double discountPercentage;
    private Integer finalPrice;
    private Boolean available;
    private Boolean official;
}
