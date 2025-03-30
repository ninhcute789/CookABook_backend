package NandK.CookABook.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import NandK.CookABook.dto.request.payment.PaymentUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.entity.Payment;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.PaymentService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách thanh toán thành công")
    public ResponseEntity<ResultPagination> getAllPayments(
            @Filter Specification<Payment> spec, Pageable pageable) {
        return ResponseEntity.ok(this.paymentService.getAllPayments(spec, pageable));
    }

    @GetMapping("/{paymentId}")
    @ApiMessage("Lấy thông tin thanh toán thành công")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long paymentId) throws IdInvalidException {
        Payment payment = this.paymentService.getPaymentById(paymentId);
        if (payment == null) {
            throw new IdInvalidException("Không tìm thấy thanh toán có id = " + paymentId);
        }
        return ResponseEntity.ok(payment);
    }

    @PutMapping
    @ApiMessage("Cập nhật trạng thái thanh toán thành công")
    public ResponseEntity<Payment> updatePaymentStatus(@Valid @RequestBody PaymentUpdateRequest request)
            throws IdInvalidException {
        Payment payment = this.paymentService.updatePaymentStatus(request);
        if (payment == null) {
            throw new IdInvalidException("Không tìm thấy thanh toán có id = " + request.getId());
        }
        return ResponseEntity.ok(payment);
    }
}
