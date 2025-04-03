package NandK.CookABook.dto.request.payment;

import NandK.CookABook.utils.constant.PaymentStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatusUpdateRequest {
    @NotNull(message = "Id thanh toán không được để trống")
    private Long id;

    private PaymentStatusEnum status;
}
