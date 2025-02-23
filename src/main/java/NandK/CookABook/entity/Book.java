package NandK.CookABook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity  //annotation tao bang
@Table(name = "books")
@Data  // Tự động tạo getter, setter, toString() nhờ Lombok
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private double price;
    private int stock;
    private String description;
    private String coverImageUrl; // URL ảnh bìa sách
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
