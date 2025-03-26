package NandK.CookABook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import NandK.CookABook.entity.Book;
import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    public CartItem findByCartAndBook(Cart cart, Book book);

    public List<CartItem> findByCart(Cart cart);
}
