package NandK.CookABook.dto.request.order;

import NandK.CookABook.utils.constant.OrderStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdateRequest {
    @NotNull(message = "Id đơn hàng không được để trống")
    private Long id;

    private OrderStatusEnum status;
}
