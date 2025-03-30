package NandK.CookABook.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.payment.PaymentUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.entity.Payment;
import NandK.CookABook.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment() {
        return null;
    }

    // TODO: add method convertToPaymentResponse

    public ResultPagination getAllPayments(Specification<Payment> spec, Pageable pageable) {
        Page<Payment> payments = this.paymentRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(payments.getTotalPages());
        meta.setTotalElements(payments.getTotalElements());
        result.setMeta(meta);

        result.setData(payments.getContent());
        return result;
    }

    public Payment getPaymentById(Long paymentId) {
        Optional<Payment> payment = this.paymentRepository.findById(paymentId);
        if (payment.isPresent()) {
            return payment.get();
        } else {
            return null;
        }
    }

    public Payment updatePaymentStatus(PaymentUpdateRequest request) {
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
}
