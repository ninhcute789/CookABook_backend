package NandK.CookABook.dto.response.payment;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import NandK.CookABook.utils.constant.PaymentMethodEnum;
import NandK.CookABook.utils.constant.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentUpdateResponse {
    private Long id;
    private Integer amount;
    private PaymentMethodEnum method;
    private PaymentStatusEnum status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
}
