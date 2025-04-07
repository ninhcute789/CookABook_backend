package NandK.CookABook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.payment.PaymentCreationRequest;
import NandK.CookABook.dto.request.payment.PaymentStatusUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.payment.PaymentCreationResponse;
import NandK.CookABook.dto.response.payment.PaymentFoundResponse;
import NandK.CookABook.dto.response.payment.PaymentUpdateResponse;
import NandK.CookABook.entity.Payment;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.PaymentRepository;
import NandK.CookABook.utils.constant.PaymentStatusEnum;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(PaymentCreationRequest request, User user) {
        Payment payment = new Payment();
        payment.setMethod(request.getMethod());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatusEnum.PENDING);
        payment.setUser(user);
        return this.paymentRepository.save(payment);
    }

    public PaymentCreationResponse convertToPaymentCreationResponse(Payment payment) {
        PaymentCreationResponse response = new PaymentCreationResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getUser().getId(),
                payment.getCreatedAt());
        return response;
    }

    public ResultPagination getAllPayments(Specification<Payment> spec, Pageable pageable) {
        Page<Payment> payments = this.paymentRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(payments.getTotalPages());
        meta.setTotalElements(payments.getTotalElements());
        result.setMeta(meta);

        List<PaymentFoundResponse> paymentResponses = new ArrayList<>();
        for (Payment payment : payments) {
            PaymentFoundResponse paymentResponse = this.convertToPaymentFoundResponse(payment);
            paymentResponses.add(paymentResponse);
        }
        result.setData(paymentResponses);
        return result;
    }

    public PaymentFoundResponse convertToPaymentFoundResponse(Payment payment) {
        PaymentFoundResponse response = new PaymentFoundResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getOrder().getId(),
                payment.getUser().getId(),
                payment.getCreatedAt(),
                payment.getUpdatedAt());
        return response;
    }

    public Payment getPaymentById(Long paymentId) {
        Optional<Payment> payment = this.paymentRepository.findById(paymentId);
        if (payment.isPresent()) {
            return payment.get();
        } else {
            return null;
        }
    }

    public Payment updatePaymentStatus(PaymentStatusUpdateRequest request) {
        Payment payment = this.getPaymentById(request.getId());
        if (payment != null) {
            if (request.getStatus() != null) {
                payment.setStatus(request.getStatus());
            }
            return this.paymentRepository.save(payment);
        } else {
            return null;
        }
    }

    public PaymentUpdateResponse convertToPaymentUpdateResponse(Payment payment) {
        PaymentUpdateResponse response = new PaymentUpdateResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getOrder().getId(),
                payment.getUser().getId(),
                payment.getUpdatedAt());
        return response;
    }

    public void deletePayment(Payment payment) {
        this.paymentRepository.delete(payment);
    }
}
