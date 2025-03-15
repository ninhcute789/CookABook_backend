package NandK.CookABook.dto.request;

import NandK.CookABook.utils.constant.CoverTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreationRequest {

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    private String publisher;
    private Integer publishYear;
    private String size;
    private Integer numberOfPages;
    private Integer weight;
    private String language;
    private String imageURL;

    @NotNull(message = "Giá gốc không được để trống")
    private Double originalPrice;

    private Double discountPercentage;
    // private Double discountPrice;

    @NotNull(message = "Số lượng không được để trống")
    private Long stockQuantity;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean available;
    // private Long sold;
    private String description;
    private CoverTypeEnum coverType;
    private Author author;
    // private Category category;

    @Getter
    @Setter
    public static class Author {
        private Long id;
    }

    // @Getter
    // @Setter
    // public static class Category {
    // private Long id;
    // }
}
