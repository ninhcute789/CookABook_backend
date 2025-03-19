package NandK.CookABook.dto.request.book;

import java.util.List;

import NandK.CookABook.utils.constant.CoverTypeEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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

    @Min(value = 1, message = "Giá sách phải là số nguyên dương")
    private Integer originalPrice;

    @DecimalMin(value = "0.0", message = "Phần trăm giảm không được âm")
    @DecimalMax(value = "100.0", message = "Phần trăm giảm không được lớn hơn 100")
    private Double discountPercentage;

    private Long stockQuantity;
    private Boolean available;
    private String description;
    private CoverTypeEnum coverType;
    private Author author;
    private List<Category> categories;

    @Getter
    @Setter
    public static class Author {
        private String name;
    }

    @Getter
    @Setter
    public static class Category {
        private Long id;
    }
}
