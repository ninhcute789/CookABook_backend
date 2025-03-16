package NandK.CookABook.dto.request;

import NandK.CookABook.utils.constant.CoverTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookUpdateRequest {
    @NotNull(message = "Id không được để trống")
    private Long id;

    private String title;
    private String publisher;
    private Integer publishYear;
    private String size;
    private Integer numberOfPages;
    private Integer weight;
    private String language;
    private String imageURL;
    private Double originalPrice;
    private Double discountPercentage;
    private Long stockQuantity;
    private Boolean available;
    private String description;
    private CoverTypeEnum coverType;
    private Author author;

    @Getter
    @Setter
    public static class Author {
        private Long id;
    }
}
