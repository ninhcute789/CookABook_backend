package NandK.CookABook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    public Cart findByUser(User user);
}
