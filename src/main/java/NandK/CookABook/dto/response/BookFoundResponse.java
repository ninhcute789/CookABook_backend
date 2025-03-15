package NandK.CookABook.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import NandK.CookABook.utils.constant.CoverTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookFoundResponse {
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
    private Double discountPrice;
    private Long stockQuantity;
    private Boolean available;
    private String description;
    private CoverTypeEnum coverType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7") // convert to GMT+7 timezone
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;

    private Author author;
    // private Category category;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Author {
        private Long id;
        private String name;
    }

    // public static class Category {
    // private Long id;
    // private String name;
    // }
}
