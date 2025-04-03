package NandK.CookABook.dto.response.shippingAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingAddressResponse {
    private Long id;
    private String name;
    private String phoneNumber;
    private String city;
    private String district;
    private String ward;
    private String address;
    private Boolean defaultAddress;
}
