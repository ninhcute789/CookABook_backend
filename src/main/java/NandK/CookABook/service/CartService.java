package NandK.CookABook.service;

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

    // // Lấy hoặc tạo giỏ hàng cho user sau khi đăng nhập
    // public Cart getOrCreateCartForUser(Long userId) {
    // User user = this.userService.getUserById(userId);
    // if (user != null) {
    // Cart cart = cartRepository.findByUser(user);
    // if (cart == null) {
    // cart = new Cart();
    // cart.setUser(user);
    // cart.setTotalQuantity(0);
    // cart.setTotalOriginalPrice(0);
    // return cartRepository.save(cart);
    // } else {
    // return getCartById(cart.getId());
    // }
    // } else {
    // return null;
    // }
    // }

    public Cart getCartById(Long cartId) {
        Optional<Cart> cart = this.cartRepository.findById(cartId);
        if (cart.isPresent()) {
            // cart.get().setTotalQuantity(this.cartItemRepository.findByCartId(cartId).size());
            // cart.get().setTotalOriginalPrice(this.cartItemRepository.findByCartId(cartId).stream()
            // .mapToInt(CartItem::getOriginalPrice).sum());
            // cart.get().setTotalDiscountPrice(this.cartItemRepository.findByCartId(cartId).stream()
            // .mapToInt(CartItem::getDiscountPrice).sum());
            // cart.get().setTotalFinalPrice(this.cartItemRepository.findByCartId(cartId).stream()
            // .mapToInt(CartItem::getFinalPrice).sum());
            // this.cartRepository.save(cart.get());
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
                // Cập nhật thông tin giỏ hàng
                cart.setTotalOriginalPrice(cart.getTotalOriginalPrice() + book.getOriginalPrice()
                        * addToCartRequest.getQuantity());
                cart.setTotalDiscountPrice(cart.getTotalDiscountPrice()
                        + (book.getOriginalPrice() - book.getFinalPrice()) * addToCartRequest.getQuantity());
                cart.setTotalFinalPrice(cart.getTotalFinalPrice() + book.getFinalPrice()
                        * addToCartRequest.getQuantity());
                this.cartRepository.save(cart);
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
                // Cập nhật thông tin giỏ hàng
                cart.setTotalQuantity(cart.getTotalQuantity() + 1);
                cart.setTotalOriginalPrice(cart.getTotalOriginalPrice() + book.getOriginalPrice()
                        * addToCartRequest.getQuantity());
                cart.setTotalDiscountPrice(cart.getTotalDiscountPrice()
                        + (book.getOriginalPrice() - book.getFinalPrice()) * addToCartRequest.getQuantity());
                cart.setTotalFinalPrice(cart.getTotalFinalPrice() + book.getFinalPrice()
                        * addToCartRequest.getQuantity());
                this.cartRepository.save(cart);
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
                new CartItemResponse.Cart(
                        cartItem.getCart().getId()),
                new CartItemResponse.Book(
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
