package NandK.CookABook.dto.request.payment;

import NandK.CookABook.utils.constant.PaymentMethodEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCreationRequest {
    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PaymentMethodEnum method;

    @NotNull(message = "Số tiền không được để trống")
    private Integer amount;

    @NotNull(message = "Id người dùng không được để trống")
    private Long userId;

}
