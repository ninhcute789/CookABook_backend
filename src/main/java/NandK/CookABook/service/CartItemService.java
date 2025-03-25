package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import NandK.CookABook.dto.response.cart.CartItemResponse;
import NandK.CookABook.entity.Book;
import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.CartItem;
import NandK.CookABook.repository.CartItemRepository;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final BookService bookService;

    public CartItemService(CartItemRepository cartItemRepository,
            CartService cartService,
            BookService bookService) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.bookService = bookService;
    }

    public CartItem getCartItemById(Long cartItemId) {
        Optional<CartItem> cartItem = this.cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {
            return cartItem.get();
        } else {
            return null;
        }
    }

    public CartItem increaseCartItemQuantity(Long cartItemId) {
        CartItem cartItem = this.getCartItemById(cartItemId);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);

            return this.cartItemRepository.save(cartItem);
        } else {
            return null;
        }
    }

    public CartItem decreaseCartItemQuantity(Long cartItemId) {
        CartItem cartItem = this.getCartItemById(cartItemId);
        if (cartItem != null) {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                return this.cartItemRepository.save(cartItem);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void deleteCartItem(Cart cart, Book book) {
        CartItem existingCartItem = this.cartItemRepository.findByCartAndBook(cart, book);
        if (existingCartItem != null) {
            this.cartItemRepository.delete(existingCartItem);
        }
    }

    public void deleteAllCartItems(Cart cart) {
        List<CartItem> cartItems = this.cartItemRepository.findByCart(cart);
        this.cartItemRepository.deleteAll(cartItems);
    }

    public List<CartItem> getCartItems(Cart cart) {
        return this.cartItemRepository.findByCart(cart);
    }

    public CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getBook().getOriginalPrice() * cartItem.getQuantity(),
                (cartItem.getBook().getOriginalPrice() - cartItem.getBook().getFinalPrice())
                        * cartItem.getQuantity(),
                cartItem.getBook().getFinalPrice() * cartItem.getQuantity(),
                new CartItemResponse.Cart(cartItem.getCart().getId()),
                new CartItemResponse.Book(
                        cartItem.getBook().getId(),
                        cartItem.getBook().getTitle(),
                        cartItem.getBook().getImageURL(),
                        cartItem.getBook().getOfficial(),
                        cartItem.getBook().getOriginalPrice(),
                        cartItem.getBook().getDiscountPercentage(),
                        cartItem.getBook().getFinalPrice()));
        return cartItemResponse;
    }
}