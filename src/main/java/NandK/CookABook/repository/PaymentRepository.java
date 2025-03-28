package NandK.CookABook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import NandK.CookABook.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
