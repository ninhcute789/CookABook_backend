package NandK.CookABook.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreationRequest {
    private String title;
    private String author;
    private Double price;
    private int stock;
    private String description;
    private String coverImageUrl;
}
