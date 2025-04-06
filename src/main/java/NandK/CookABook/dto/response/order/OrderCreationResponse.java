package NandK.CookABook.dto.response.order;

import java.util.List;

import NandK.CookABook.utils.constant.OrderStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreationResponse {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private Integer totalQuantity;
    private Integer totalFinalPrice;
    private OrderStatusEnum status;
    private List<OrderItemPreviewResponse> orderItems;
}
