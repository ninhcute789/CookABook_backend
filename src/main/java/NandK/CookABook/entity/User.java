package NandK.CookABook.entity;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import NandK.CookABook.utils.SecurityUtil;
import NandK.CookABook.utils.constant.GenderEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING) // luu gia tri cua Enum vao database theo dang String
    private GenderEnum gender; // tap hop cac hang so gioi tinh

    private LocalDate dob;
    private String email;
    // private String avatar;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void beforeCreate() {
        this.setCreatedBy(
                SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
                        : "anonymous");
        this.setCreatedAt(Instant.now());
    }

    @PreUpdate
    public void beforeUpdate() {
        this.setUpdatedBy(
                SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
                        : "anonymous");
        this.setUpdatedAt(Instant.now());
    }
}
