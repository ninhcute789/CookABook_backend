package NandK.CookABook.dto.request.book;

import java.util.List;

import NandK.CookABook.utils.constant.CoverTypeEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
    @Min(value = 1, message = "Giá gốc phải là số nguyên dương")
    private Integer originalPrice;

    @NotNull(message = "Phần trăm giảm không được để trống, không giảm thì nhập 0")
    @DecimalMin(value = "0.0", message = "Phần trăm giảm không được âm")
    @DecimalMax(value = "100.0", message = "Phần trăm giảm không được lớn hơn 100")
    private Double discountPercentage;

    @NotNull(message = "Số lượng không được để trống")
    private Integer stockQuantity;

    @NotNull(message = "Trạng thái hàng không được để trống")
    private Boolean available;

    @NotNull(message = "Trạng thái chính hãng không được để trống")
    private Boolean official;
    // private Long sold;
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
