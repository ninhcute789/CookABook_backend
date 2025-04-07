package NandK.CookABook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import NandK.CookABook.dto.request.order.OrderCreationFromUserIdRequest;
import NandK.CookABook.dto.request.order.OrderStatusUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.cart.CartItemResponse;
import NandK.CookABook.dto.response.order.OrderCreationResponse;
import NandK.CookABook.dto.response.order.OrderFoundResponse;
import NandK.CookABook.dto.response.order.OrderStatusUpdateResponse;
import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.Order;
import NandK.CookABook.entity.Payment;
import NandK.CookABook.entity.ShippingAddress;
import NandK.CookABook.entity.User;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.CartService;
import NandK.CookABook.service.OrderService;
import NandK.CookABook.service.PaymentService;
import NandK.CookABook.service.ShippingAddressService;
import NandK.CookABook.service.UserService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")

public class OrderController {

    private HttpSession session;
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final ShippingAddressService shippingAddressService;
    private final PaymentService paymentService;

    public OrderController(HttpSession session, OrderService orderService, UserService userService,
            CartService cartService, ShippingAddressService shippingAddressService, PaymentService paymentService) {
        this.session = session;
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
        this.shippingAddressService = shippingAddressService;
        this.paymentService = paymentService;
    }

    @PostMapping("/save-cart-to-session/{cartId}")
    @ApiMessage("Lưu giỏ hàng thành công")
    public ResponseEntity<Cart> saveCart(@PathVariable Long cartId) throws IdInvalidException {
        Cart cart = this.cartService.getCartById(cartId);
        if (cart == null) {
            throw new IdInvalidException("Giỏ hàng với id = " + cartId + " không tồn tại");
        }
        if (cart.getTotalQuantity() == 0) {
            throw new IdInvalidException("Giỏ hàng với id = " + cartId + " không có sản phẩm nào được chọn");
        }
        session.removeAttribute("cart"); // Xoá giỏ hàng cũ trong session nếu có
        session.setAttribute("cart", cart); // Lưu giỏ hàng vào session
        this.cartService.saveCart(cart);
        return ResponseEntity.ok((Cart) session.getAttribute("cart"));
    }

    @PostMapping("/save-shipping-address-to-session/{shippingAddressId}")
    @ApiMessage("Lưu địa chỉ giao hàng thành công")
    public ResponseEntity<ShippingAddress> saveShippingAddress(@PathVariable Long shippingAddressId)
            throws IdInvalidException {
        ShippingAddress shippingAddress = this.shippingAddressService.getShippingAddressById(shippingAddressId);
        if (shippingAddress == null) {
            throw new IdInvalidException("Địa chỉ giao hàng với id = " + shippingAddressId + " không tồn tại");
        }
        session.removeAttribute("shippingAddress"); // Xoá địa chỉ giao hàng cũ trong session nếu có
        session.setAttribute("shippingAddress", shippingAddress); // Lưu địa chỉ giao hàng vào session
        return ResponseEntity.ok((ShippingAddress) session.getAttribute("shippingAddress"));
    }

    @PostMapping("/save-payment-to-session/{paymentId}")
    @ApiMessage("Lưu thông tin thanh toán thành công")
    public ResponseEntity<Payment> savePayment(@PathVariable Long paymentId) throws IdInvalidException {
        Payment payment = this.paymentService.getPaymentById(paymentId);
        if (payment == null) {
            throw new IdInvalidException("Thông tin thanh toán với id = " + paymentId + " không tồn tại");
        }
        if (payment.getOrder() != null) {
            throw new IdInvalidException("Thông tin thanh toán với id = " + paymentId
                    + " đã được sử dụng cho đơn hàng khác");
        }
        session.removeAttribute("paymentMethod"); // Xoá thông tin thanh toán cũ trong session nếu có
        session.setAttribute("payment", payment); // Lưu thông tin thanh toán vào session
        return ResponseEntity.ok((Payment) session.getAttribute("payment"));
    }

    @GetMapping("/session")
    @ApiMessage("Lấy thông tin session thành công")
    public ResponseEntity<Map<String, Object>> getSessionInfo() {
        Map<String, Object> sessionInfo = new HashMap<>();

        Cart cart = (Cart) session.getAttribute("cart");
        ShippingAddress shippingAddress = (ShippingAddress) session.getAttribute("shippingAddress");
        Payment payment = (Payment) session.getAttribute("payment");

        sessionInfo.put("cart", cart);
        sessionInfo.put("shippingAddress", shippingAddress);
        sessionInfo.put("payment", payment);

        return ResponseEntity.ok(sessionInfo);
    }

    @PostMapping
    @ApiMessage("Tạo đơn hàng thành công")
    public ResponseEntity<OrderCreationResponse> createOrderFromUserId(
            @Valid @RequestBody OrderCreationFromUserIdRequest request) throws IdInvalidException {
        User user = this.userService.getUserById(request.getUserId());
        if (user == null) {
            throw new IdInvalidException("Người dùng với id = " + request.getUserId() + " không tồn tại");
        }
        Cart cartFromSession = (Cart) session.getAttribute("cart");
        if (cartFromSession == null) {
            throw new IdInvalidException("Giỏ hàng chưa được lưu");
        }
        ShippingAddress shippingAddressFromSession = (ShippingAddress) session.getAttribute("shippingAddress");
        if (shippingAddressFromSession == null) {
            throw new IdInvalidException("Địa chỉ giao hàng chưa được lưu");
        }
        Payment paymentFromSession = (Payment) session.getAttribute("payment");
        if (paymentFromSession == null) {
            throw new IdInvalidException("Thông tin thanh toán chưa được lưu");
        }
        Order order = this.orderService.createOrder(user, cartFromSession, shippingAddressFromSession,
                paymentFromSession);
        session.removeAttribute("cart"); // Xoá giỏ hàng trong session
        session.removeAttribute("shippingAddress"); // Xoá địa chỉ giao hàng trong session
        session.removeAttribute("payment"); // Xoá thông tin thanh toán trong session
        return ResponseEntity.ok(this.orderService.convertToOrderCreationResponse(order));
    }

    @GetMapping
    @ApiMessage("Lấy danh sách đơn hàng thành công")
    public ResponseEntity<ResultPagination> getAllOrders(@Filter Specification<Order> spec, Pageable pageable) {
        return ResponseEntity.ok(this.orderService.getAllOrders(spec, pageable));
    }

    @GetMapping("/{orderId}")
    @ApiMessage("Lấy thông tin đơn hàng thành công")
    public ResponseEntity<OrderFoundResponse> getOrderById(@PathVariable Long orderId)
            throws IdInvalidException {
        Order order = this.orderService.getOrderById(orderId);
        if (order == null) {
            throw new IdInvalidException("Đơn hàng với id = " + orderId + " không tồn tại");
        }
        return ResponseEntity.ok(this.orderService.convertToOrderFoundResponse(order));
    }

    @PutMapping
    @ApiMessage("Cập nhật trạng thái đơn hàng thành công")
    public ResponseEntity<OrderStatusUpdateResponse> updateOrderStatus(
            @Valid @RequestBody OrderStatusUpdateRequest request) throws IdInvalidException {
        Order order = this.orderService.updateOrderStatus(request);
        if (order == null) {
            throw new IdInvalidException("Đơn hàng với id = " + request.getId() + " không tồn tại");
        }
        return ResponseEntity.ok(this.orderService.convertToOrderStatusUpdateResponse(order));
    }

    @PostMapping("/{orderId}/cancel")
    @ApiMessage("Huỷ đơn hàng thành công")
    public ResponseEntity<String> cancelOrderById(@PathVariable Long orderId) throws IdInvalidException {
        Order order = this.orderService.getOrderById(orderId);
        if (order == null) {
            throw new IdInvalidException("Đơn hàng với id = " + orderId + " không tồn tại");
        }
        this.orderService.cancelOrder(order);
        return ResponseEntity.ok("Đơn hàng với id = " + orderId + " đã được huỷ thành công");
    }

    @PostMapping("/{orderId}/reorder")
    @ApiMessage("Thêm các sản phẩm trong đơn hàng vào giỏ hàng thành công")
    public ResponseEntity<List<CartItemResponse>> reorderByOrderId(@PathVariable Long orderId)
            throws IdInvalidException {
        Order order = this.orderService.getOrderById(orderId);
        if (order == null) {
            throw new IdInvalidException("Đơn hàng với id = " + orderId + " không tồn tại");
        }
        List<CartItemResponse> cartItemResponses = this.orderService.reorder(order);
        if (cartItemResponses == null) {
            throw new IdInvalidException("Đã xảy ra lỗi trong quá trình reorder với đơn hàng có id = " + orderId);
        }
        return ResponseEntity.ok(cartItemResponses);
    }

    @DeleteMapping("/{orderId}")
    @ApiMessage("Xoá đơn hàng thành công")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long orderId) throws IdInvalidException {
        Order order = this.orderService.getOrderById(orderId);
        if (order == null) {
            throw new IdInvalidException("Đơn hàng với id = " + orderId + " không tồn tại");
        }
        this.orderService.deleteOrder(order);
        return ResponseEntity.ok(null);
    }
}
