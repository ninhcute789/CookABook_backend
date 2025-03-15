package NandK.CookABook.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import NandK.CookABook.utils.constant.CoverTypeEnum;
import lombok.Getter;
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
    private Double originalPrice;
    private Double discountPercentage;
    private Double discountPrice;
    private Long stockQuantity;
    private Boolean available;
    private String description;
    private CoverTypeEnum coverType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;

    private Author author;

    @Getter
    @Setter
    public static class Author {
        private Long id;
        private String name;
    }
}
