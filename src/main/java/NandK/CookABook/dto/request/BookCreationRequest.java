package NandK.CookABook.dto.request;

import lombok.Data;

@Data
public class BookCreationRequest {
    private String title;
    private String author;
    private Double price;
    private String description;
}
