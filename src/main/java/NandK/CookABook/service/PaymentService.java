package NandK.CookABook.service;

import org.springframework.stereotype.Service;

import NandK.CookABook.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

}
