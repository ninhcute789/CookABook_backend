package NandK.CookABook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import NandK.CookABook.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

}
