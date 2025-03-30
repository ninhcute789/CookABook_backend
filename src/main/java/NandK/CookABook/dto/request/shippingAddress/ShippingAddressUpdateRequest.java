package NandK.CookABook.dto.request.shippingAddress;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingAddressUpdateRequest {
    @NotNull(message = "Id địa chỉ không được để trống")
    private Long id;

    private String name;
    private String phoneNumber;
    private String city;
    private String district;
    private String ward;
    private String address;
    private Boolean defaultAddress;
}
