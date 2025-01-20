package NandK.CookABook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import NandK.CookABook.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
