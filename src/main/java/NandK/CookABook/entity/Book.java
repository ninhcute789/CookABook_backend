package NandK.CookABook.entity;

import java.time.Instant;

import jakarta.persistence.Column;
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

    @Column(columnDefinition = "MEDIUMTEXT") // Để lưu trữ dữ liệu dạng text dài
    private String description;

    private double price;
    private int stock;
    private String imageURL; // URL ảnh bìa sách
    private Instant createdAt;
    private Instant updatedAt;
    private String createBy;
    private String updateBy;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
