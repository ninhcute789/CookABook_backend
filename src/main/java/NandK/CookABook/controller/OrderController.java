package NandK.CookABook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.request.order.OrderCreationRequest;
import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.Order;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.CartService;
import NandK.CookABook.service.OrderService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/orders")

public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PostMapping
    @ApiMessage("Tạo đơn hàng thành công")
    public ResponseEntity<Void> createOrderFromCart(@Valid @RequestBody OrderCreationRequest request)
            throws IdInvalidException {
        Cart cart = this.cartService.getCartById(request.getCartId());
        if (cart == null) {
            throw new IdInvalidException("Giỏ hàng với id = " + request.getCartId() + " không hợp lệ");
        }

        return ResponseEntity.ok(null);
    }
}
