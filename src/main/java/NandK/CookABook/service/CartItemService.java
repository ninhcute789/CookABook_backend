package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import NandK.CookABook.dto.response.cart.CartItemResponse;
import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.CartItem;
import NandK.CookABook.repository.CartItemRepository;
import NandK.CookABook.repository.CartRepository;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    public CartItem getCartItemById(Long cartItemId) {
        Optional<CartItem> cartItem = this.cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {
            return cartItem.get();
        } else {
            return null;
        }
    }

    public void calculatePrice(CartItem cartItem) {
        cartItem.setOriginalPrice(cartItem.getBook().getOriginalPrice() * cartItem.getQuantity());
        cartItem.setDiscountPrice(
                (cartItem.getBook().getOriginalPrice() - cartItem.getBook().getFinalPrice())
                        * cartItem.getQuantity());
        cartItem.setFinalPrice(cartItem.getBook().getFinalPrice() * cartItem.getQuantity());
    }

    public CartItem increaseCartItemQuantityById(Long cartItemId) {
        CartItem cartItem = this.getCartItemById(cartItemId);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            this.calculatePrice(cartItem);
            return this.cartItemRepository.save(cartItem);
        } else {
            return null;
        }
    }

    public CartItem decreaseCartItemQuantityById(Long cartItemId) {
        CartItem cartItem = this.getCartItemById(cartItemId);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            this.calculatePrice(cartItem);
            return this.cartItemRepository.save(cartItem);
        } else {
            return null;
        }
    }

    public void deleteCartItem(CartItem cartItem) {
        Cart cart = cartItem.getCart();
        cart.setTotalQuantity(cart.getTotalQuantity() - 1);
        cart.setTotalOriginalPrice(cart.getTotalOriginalPrice() - cartItem.getOriginalPrice());
        cart.setTotalDiscountPrice(cart.getTotalDiscountPrice() - cartItem.getDiscountPrice());
        cart.setTotalFinalPrice(cart.getTotalFinalPrice() - cartItem.getFinalPrice());
        this.cartRepository.save(cart);
        this.cartItemRepository.delete(cartItem);
    }

    public List<CartItem> getCartItems(Cart cart) {
        return this.cartItemRepository.findByCart(cart);
    }

    public CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getOriginalPrice(),
                cartItem.getDiscountPrice(),
                cartItem.getFinalPrice(),
                cartItem.getSelected(),
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

    // Cập nhật trạng thái selected cho CartItem
    public void updateCartItemSelection(CartItem cartItem) {
        cartItem.setSelected(!cartItem.getSelected()); // Đảo ngược trạng thái selected
        cartItemRepository.save(cartItem); // Lưu lại thay đổi
    }
}