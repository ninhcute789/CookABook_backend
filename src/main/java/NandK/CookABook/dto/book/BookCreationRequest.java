package NandK.CookABook.dto.book;

import java.time.Instant;

import NandK.CookABook.entity.Author;
import NandK.CookABook.entity.Category;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreationRequest {

    @NotBlank(message = "Tên sách không được để trống")
    private String title;

    @NotBlank(message = "Tác giả không được để trống")
    private Author author;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @NotBlank(message = "Giá không được để trống")
    private long price;

    @NotBlank(message = "Số lượng không được để trống")
    private int stock;

    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private String createBy;
    private String updateBy;

    @NotBlank(message = "Thể loại không được để trống")
    private Category category;
}
