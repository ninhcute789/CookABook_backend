package NandK.CookABook.controller;

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

import NandK.CookABook.dto.request.payment.PaymentCreationRequest;
import NandK.CookABook.dto.request.payment.PaymentStatusUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.cart.CartPaymentResponse;
import NandK.CookABook.dto.response.payment.PaymentCreationResponse;
import NandK.CookABook.dto.response.payment.PaymentFoundResponse;
import NandK.CookABook.dto.response.payment.PaymentUpdateResponse;
import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.Payment;
import NandK.CookABook.entity.User;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.CartService;
import NandK.CookABook.service.PaymentService;
import NandK.CookABook.service.UserService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final CartService cartService;

    public PaymentController(PaymentService paymentService, UserService userService, CartService cartService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @PostMapping
    @ApiMessage("Tạo thanh toán thành công")
    public ResponseEntity<PaymentCreationResponse> createPayment(@Valid @RequestBody PaymentCreationRequest request)
            throws IdInvalidException {
        User user = this.userService.getUserById(request.getUserId());
        if (user == null) {
            throw new IdInvalidException("Không tìm thấy người dùng có id = " + request.getUserId());
        }
        Payment payment = this.paymentService.createPayment(request, user);
        return ResponseEntity.ok(this.paymentService.convertToPaymentCreationResponse(payment));
    }

    // TODO: @GetMapping("/user/{userId}")

    @GetMapping
    @ApiMessage("Lấy danh sách thanh toán thành công")
    public ResponseEntity<ResultPagination> getAllPayments(
            @Filter Specification<Payment> spec, Pageable pageable) {
        return ResponseEntity.ok(this.paymentService.getAllPayments(spec, pageable));
    }

    @GetMapping("/{paymentId}")
    @ApiMessage("Lấy thông tin thanh toán thành công")
    public ResponseEntity<PaymentFoundResponse> getPaymentById(@PathVariable Long paymentId) throws IdInvalidException {
        Payment payment = this.paymentService.getPaymentById(paymentId);
        if (payment == null) {
            throw new IdInvalidException("Không tìm thấy thanh toán có id = " + paymentId);
        }
        return ResponseEntity.ok(this.paymentService.convertToPaymentFoundResponse(payment));
    }

    @GetMapping("/get-cart/{cartId}")
    @ApiMessage("Lấy giỏ hàng thanh toán thành công")
    public ResponseEntity<CartPaymentResponse> getCartPaymentById(@PathVariable Long cartId)
            throws IdInvalidException {
        Cart cart = this.cartService.getCartById(cartId);
        if (cart == null) {
            throw new IdInvalidException("Giỏ hàng với id = " + cartId + " không hợp lệ");
        }
        // this.cartService.calculateCartPrice(cart);
        return ResponseEntity.ok(this.cartService.convertToCartPaymentResponse(cart));
    }

    @PutMapping
    @ApiMessage("Cập nhật trạng thái thanh toán thành công")
    public ResponseEntity<PaymentUpdateResponse> updatePaymentStatus(
            @Valid @RequestBody PaymentStatusUpdateRequest request)
            throws IdInvalidException {
        Payment payment = this.paymentService.updatePaymentStatus(request);
        if (payment == null) {
            throw new IdInvalidException("Không tìm thấy thanh toán có id = " + request.getId());
        }
        return ResponseEntity.ok(this.paymentService.convertToPaymentUpdateResponse(payment));
    }

    @DeleteMapping("/{paymentId}")
    @ApiMessage("Xóa thanh toán thành công")
    public ResponseEntity<Void> deletePayment(@PathVariable Long paymentId) throws IdInvalidException {
        Payment payment = this.paymentService.getPaymentById(paymentId);
        if (payment == null) {
            throw new IdInvalidException("Không tìm thấy thanh toán có id = " + paymentId);
        }
        this.paymentService.deletePayment(payment);
        return ResponseEntity.ok(null);
    }
}
