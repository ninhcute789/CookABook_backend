package NandK.CookABook.dto.request.shippingAddress;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingAddressCreationRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber;

    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    private String city;

    @NotBlank(message = "Quận/Huyện không được để trống")
    private String district;

    @NotBlank(message = "Phường/Xã không được để trống")
    private String ward;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotNull(message = "Địa chỉ mặc định không được để null")
    private Boolean defaultAddress;

    @NotNull(message = "Id người dùng không được để trống")
    private Long userId;
}
