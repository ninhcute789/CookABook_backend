package NandK.CookABook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.request.cart.AddToCartRequest;
import NandK.CookABook.dto.response.cart.CartItemResponse;
import NandK.CookABook.dto.response.cart.CartPreviewResponse;
import NandK.CookABook.entity.Cart;
import NandK.CookABook.entity.CartItem;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.CartItemService;
import NandK.CookABook.service.CartService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    public CartController(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/{cartId}/quantity")
    @ApiMessage("Lấy số lượng sản phẩm trong giỏ hàng thành công")
    public ResponseEntity<Integer> getCartSizeById(@PathVariable Long cartId) throws IdInvalidException {
        Cart cart = this.cartService.getCartById(cartId);
        if (cart == null) {
            throw new IdInvalidException("Giỏ hàng với id = " + cartId + " không hợp lệ");
        }
        return ResponseEntity.ok(this.cartService.getCartSize(cart));
    }

    @GetMapping("{cartId}")
    @ApiMessage("Lấy giỏ hàng thành công")
    public ResponseEntity<CartPreviewResponse> getCartById(@PathVariable Long cartId)
            throws IdInvalidException {
        Cart cart = this.cartService.getCartById(cartId);
        if (cart == null) {
            throw new IdInvalidException("Giỏ hàng với id = " + cartId + " không hợp lệ");
        }
        this.cartService.calculateCartPrice(cart);
        return ResponseEntity.ok(this.cartService.convertToCartPreviewResponse(cart));
    }

    @PostMapping("/add")
    @ApiMessage("Thêm sản phẩm vào giỏ hàng thành công")
    public ResponseEntity<CartItemResponse> addToCart(@Valid @RequestBody AddToCartRequest request)
            throws IdInvalidException {
        CartItem cartItem = this.cartService.addToCart(request);
        if (cartItem == null) {
            throw new IdInvalidException("Giỏ hàng với id = " + request.getCartId() + " hoặc sách với id = "
                    + request.getBookId() + " không hợp lệ");
        }
        return ResponseEntity.ok(this.cartItemService.convertToCartItemResponse(cartItem));
    }

    @DeleteMapping("{cartId}")
    @ApiMessage("Xóa tất cả sản phẩm trong giỏ hàng thành công")
    public ResponseEntity<String> deleteAllCartItems(@PathVariable Long cartId) throws IdInvalidException {
        Cart cart = this.cartService.getCartById(cartId);
        if (cart == null) {
            throw new IdInvalidException("Giỏ hàng với id = " + cartId + " không hợp lệ");
        }
        this.cartService.deleteAllCartItems(cart);
        return ResponseEntity.ok("Xóa tất cả sản phẩm trong giỏ hàng thành công");
    }
}
