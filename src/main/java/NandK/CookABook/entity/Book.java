package NandK.CookABook.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String description;
    private String publisher;
    private String publishYear;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String imageURL;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private BigDecimal discountPercentage;
    private Long stockQuantity;
    private Double rating;
    private Long ratingCount;
    private Long sold;
    private Instant createdAt;
    private Instant updatedAt;

}
