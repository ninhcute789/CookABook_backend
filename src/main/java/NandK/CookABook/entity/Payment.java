package NandK.CookABook.entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import NandK.CookABook.utils.constant.PaymentMethodEnum;
import NandK.CookABook.utils.constant.PaymentStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer amount;
    private PaymentMethodEnum method;
    private PaymentStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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
