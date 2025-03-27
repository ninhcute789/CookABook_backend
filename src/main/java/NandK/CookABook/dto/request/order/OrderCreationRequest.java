package NandK.CookABook.dto.request.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreationRequest {
    @NotNull(message = "Id giỏ hàng không được để trống")
    private Long cartId;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber;

    private String email;
}
