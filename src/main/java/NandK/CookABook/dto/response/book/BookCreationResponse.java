package NandK.CookABook.dto.response.book;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import NandK.CookABook.utils.constant.CoverTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class BookCreationResponse {
    private Long id;
    private String title;
    private String publisher;
    private Integer publishYear;
    private String size;
    private Integer numberOfPages;
    private Integer weight;
    private String language;
    private String imageURL;
    private Integer originalPrice;
    private Double discountPercentage;
    private Integer finalPrice;
    private Integer stockQuantity;
    private Boolean available;
    private Boolean official;
    // private Integer sold;
    private String description;
    private CoverTypeEnum coverType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt;

    private Author author;

    @Getter
    @Setter
    public static class Author {
        private Long id;
        private String name;
    }

    private List<Category> categories;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category {
        private Long id;
        private String name;
    }
}
