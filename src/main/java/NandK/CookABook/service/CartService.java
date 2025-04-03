package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.cart.AddToCartRequest;
import NandK.CookABook.dto.response.cart.CartItemResponse;
import NandK.CookABook.dto.response.cart.CartPreviewResponse;
import NandK.CookABook.entity.Book;
import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.CartItem;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.CartItemRepository;
import NandK.CookABook.repository.CartRepository;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final BookService bookService;
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            BookService bookService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookService = bookService;
    }

    // Tạo giỏ hàng mới cho user lần đầu đăng ký
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalQuantity(0);
        cart.setTotalOriginalPrice(0);
        cart.setTotalDiscountPrice(0);
        cart.setTotalFinalPrice(0);
        return this.cartRepository.save(cart);
    }

    public Cart getCartById(Long cartId) {
        Optional<Cart> cart = this.cartRepository.findById(cartId);
        if (cart.isPresent()) {
            return cart.get();
        } else {
            return null;
        }
    }

    // Lấy số lượng sản phẩm trong giỏ hàng để hiển thị trên giỏ hàng
    public Integer getTotalQuantityById(Long cartId) {
        Cart cart = this.getCartById(cartId);
        if (cart != null) {
            return cart.getTotalQuantity();
        } else {
            return null;
        }
    }

    // Thêm sách vào giỏ hàng hoặc cập nhật số lượng
    public CartItem addToCart(AddToCartRequest addToCartRequest) {
        Cart cart = this.getCartById(addToCartRequest.getCartId());
        Book book = this.bookService.getBookById(addToCartRequest.getBookId());

        if (cart != null && book != null) {
            CartItem existingCartItem = this.cartItemRepository.findByCartAndBook(cart, book);
            if (existingCartItem != null) {
                // Nếu đã có, xử lý cập nhật số lượng
                existingCartItem.setQuantity(existingCartItem.getQuantity() + addToCartRequest.getQuantity());
                existingCartItem.setOriginalPrice(book.getOriginalPrice() * existingCartItem.getQuantity());
                existingCartItem.setDiscountPrice(
                        (book.getOriginalPrice() - book.getFinalPrice()) * existingCartItem.getQuantity());
                existingCartItem.setFinalPrice(book.getFinalPrice() * existingCartItem.getQuantity());
                existingCartItem.setSelected(true);
                // Cập nhật thông tin CartItem
                return this.cartItemRepository.save(existingCartItem);
            } else {
                // Nếu chưa có, tạo mới CartItem
                CartItem newCartItem = new CartItem();
                newCartItem.setCart(cart);
                newCartItem.setBook(book);
                newCartItem.setQuantity(addToCartRequest.getQuantity());
                newCartItem.setOriginalPrice(book.getOriginalPrice() * addToCartRequest.getQuantity());
                newCartItem.setDiscountPrice(
                        (book.getOriginalPrice() - book.getFinalPrice()) * addToCartRequest.getQuantity());
                newCartItem.setFinalPrice(book.getFinalPrice() * addToCartRequest.getQuantity());
                newCartItem.setSelected(true);
                // Cập nhật số lượng sản phẩm trong giỏ hàng
                cart.setTotalQuantity(cart.getTotalQuantity() + 1);
                // Lưu thông tin CartItem
                return this.cartItemRepository.save(newCartItem);
            }
        } else {
            return null;
        }
    }

    // Chuyển thông tin giỏ hàng sang dạng response
    public CartPreviewResponse convertToCartPreviewResponse(Cart cart) {
        CartPreviewResponse cartPreviewResponse = new CartPreviewResponse();
        cartPreviewResponse.setId(cart.getId());
        cartPreviewResponse.setTotalQuantity(cart.getTotalQuantity());
        cartPreviewResponse.setTotalOriginalPrice(cart.getTotalOriginalPrice());
        cartPreviewResponse.setTotalFinalPrice(cart.getTotalFinalPrice());
        cartPreviewResponse.setTotalDiscountPrice(cart.getTotalDiscountPrice());
        // Lấy thông tin sách trong giỏ hàng
        cartPreviewResponse.setCartItems(cart.getCartItems().stream().map(cartItem -> new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getOriginalPrice(),
                cartItem.getDiscountPrice(),
                cartItem.getFinalPrice(),
                cartItem.getSelected(),
                new CartItemResponse.BookResponse(
                        cartItem.getBook().getId(),
                        cartItem.getBook().getTitle(),
                        cartItem.getBook().getImageURL(),
                        cartItem.getBook().getOfficial(),
                        cartItem.getBook().getOriginalPrice(),
                        cartItem.getBook().getDiscountPercentage(),
                        cartItem.getBook().getFinalPrice())))
                .toList());
        return cartPreviewResponse;
    }

    // Lấy tất cả sản phẩm được chọn trong giỏ hàng
    public List<CartItem> getSelectedItems(Cart cart) {
        return this.cartItemRepository.findByCartAndSelected(cart, true);
    }

    // Xóa tất cả sản phẩm được chọn trong giỏ hàng
    public void deleteSelectedItems(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            this.cartItemRepository.delete(cartItem);
        }
    }

    // Tính tổng tiền giỏ hàng chỉ cho những sản phẩm được chọn
    public void calculateCartPrice(Cart cart) {
        List<CartItem> cartItems = this.getSelectedItems(cart);
        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                cart.setTotalOriginalPrice(cart.getTotalOriginalPrice() + cartItem.getOriginalPrice());
                cart.setTotalDiscountPrice(cart.getTotalDiscountPrice() + cartItem.getDiscountPrice());
                cart.setTotalFinalPrice(cart.getTotalFinalPrice() + cartItem.getFinalPrice());
            }
        }
    }

    public void resetCartToZero(Cart cart) {
        cart.setTotalQuantity(0);
        cart.setTotalOriginalPrice(0);
        cart.setTotalDiscountPrice(0);
        cart.setTotalFinalPrice(0);
        this.cartRepository.save(cart);
    }

    // Lưu thông tin giỏ hàng
    public void saveCart(Cart cart) {
        // Tính toán lại tổng tiền giỏ hàng
        cart.setTotalOriginalPrice(0);
        cart.setTotalDiscountPrice(0);
        cart.setTotalFinalPrice(0);
        this.calculateCartPrice(cart);
        // Lưu thông tin giỏ hàng
        this.cartRepository.save(cart);
    }

    // Xóa tất cả sản phẩm trong giỏ hàng
    public void deleteAllCartItems(Cart cart) {
        cart.setTotalQuantity(0);
        cart.setTotalOriginalPrice(0);
        cart.setTotalDiscountPrice(0);
        cart.setTotalFinalPrice(0);
        this.cartRepository.save(cart);
        this.cartItemRepository.deleteAll(cart.getCartItems());
    }
}
