package NandK.CookABook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.response.cart.CartItemResponse;
import NandK.CookABook.entity.CartItem;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.CartItemService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/{cartItemId}/update-selected")
    @ApiMessage("Cập nhật trạng thái sản phẩm trong giỏ hàng thành công")
    public ResponseEntity<Void> updateCartItemSelection(@Valid @PathVariable Long cartItemId)
            throws IdInvalidException {
        CartItem cartItem = this.cartItemService.getCartItemById(cartItemId);
        if (cartItem == null) {
            throw new IdInvalidException("Cart item với id = " + cartItemId + " không tồn tại");
        }
        this.cartItemService.updateCartItemSelection(cartItem);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{cartItemId}")
    @ApiMessage("Lấy thông tin chi tiết sản phẩm trong giỏ hàng thành công")
    public ResponseEntity<CartItemResponse> getCartItemById(@Valid @PathVariable Long cartItemId)
            throws IdInvalidException {
        CartItem cartItem = this.cartItemService.getCartItemById(cartItemId);
        if (cartItem == null) {
            throw new IdInvalidException("Cart item với id = " + cartItemId + " không tồn tại");
        }
        return ResponseEntity.ok(this.cartItemService.convertToCartItemResponse(cartItem));
    }

    @GetMapping("/{cartItemId}/increase")
    @ApiMessage("Tăng số lượng sản phẩm trong giỏ hàng thành công")
    public ResponseEntity<CartItemResponse> increaseCartItemQuantityById(@Valid @PathVariable Long cartItemId)
            throws IdInvalidException {
        CartItem cartItem = this.cartItemService.increaseCartItemQuantityById(cartItemId);
        if (cartItem == null) {
            throw new IdInvalidException("Cart item với id = " + cartItemId + " không tồn tại");
        }
        return ResponseEntity.ok(this.cartItemService.convertToCartItemResponse(cartItem));
    }

    @GetMapping("/{cartItemId}/decrease")
    @ApiMessage("Giảm số lượng sản phẩm trong giỏ hàng thành công")
    public ResponseEntity<CartItemResponse> decreaseCartItemQuantityById(@Valid @PathVariable Long cartItemId)
            throws IdInvalidException {
        CartItem cartItem = this.cartItemService.decreaseCartItemQuantityById(cartItemId);
        if (cartItem == null) {
            throw new IdInvalidException("Cart item với id = " + cartItemId + " không tồn tại");
        }
        if (cartItem.getQuantity() == 0) {
            this.cartItemService.deleteCartItem(cartItem);
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(this.cartItemService.convertToCartItemResponse(cartItem));
    }

    @DeleteMapping("/{cartItemId}")
    @ApiMessage("Xóa sản phẩm khỏi giỏ hàng thành công")
    public ResponseEntity<Void> deleteCartItemById(@Valid @PathVariable Long cartItemId) throws IdInvalidException {
        CartItem cartItem = this.cartItemService.getCartItemById(cartItemId);
        if (cartItem == null) {
            throw new IdInvalidException("Cart item với id = " + cartItemId + " không tồn tại");
        }
        this.cartItemService.deleteCartItem(cartItem);
        return ResponseEntity.ok(null);
    }
}
