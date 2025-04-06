package NandK.CookABook.dto.response.order;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import NandK.CookABook.utils.constant.OrderStatusEnum;
import NandK.CookABook.utils.constant.PaymentMethodEnum;
import NandK.CookABook.utils.constant.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class OrderFoundResponse {
    private Long id;
    private Integer totalQuantity;
    private Integer totalPrice;
    private OrderStatusEnum status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;

    private Long userId;
    private ShippingAddress shippingAddress;
    private Payment payment;
    private List<OrderItemFoundResponse> orderItems;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShippingAddress {
        private Long id;
        private String name;
        private String phoneNumber;
        private String city;
        private String district;
        private String ward;
        private String address;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payment {
        private Long id;
        private PaymentMethodEnum paymentMethod;
        private PaymentStatusEnum paymentStatus;
    }
}
