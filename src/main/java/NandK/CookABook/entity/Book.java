package NandK.CookABook.entity;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import NandK.CookABook.utils.constant.CoverTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
    private String publisher;
    private Integer publishYear;
    private String size;
    private Integer numberOfPages;
    private Integer weight;
    private String language;
    private String imageURL;
    private Integer originalPrice;
    private Double discountPercentage;
    private Integer discountPrice;
    private Long stockQuantity;
    private Boolean available;
    // private Double rating;
    // private Long ratingCount;
    // private Long sold;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private CoverTypeEnum coverType;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Category> categories;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;

    @PrePersist
    public void beforeCreate() {
        this.setCreatedAt(Instant.now());
    }

    @PreUpdate
    public void beforeUpdate() {
        this.setUpdatedAt(Instant.now());
    }

}
