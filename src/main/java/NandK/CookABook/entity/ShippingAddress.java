package NandK.CookABook.entity;

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
@Table(name = "shipping_addresses")
@Getter
@Setter
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;
    private String city; // thành phố
    private String district; // quận
    private String ward; // phường
    private String address; // địa chỉ cụ thể
    private Boolean defaultAddress = false; // địa chỉ mặc định

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
