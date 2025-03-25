package NandK.CookABook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("{cartItemId}")
    @ApiMessage("Lấy thông tin chi tiết sản phẩm trong giỏ hàng thành công")
    public ResponseEntity<CartItemResponse> getCartItemById(@Valid @PathVariable Long cartItemId)
            throws IdInvalidException {
        CartItem cartItem = this.cartItemService.getCartItemById(cartItemId);
        if (cartItem == null) {
            throw new IdInvalidException("Cart item id không hợp lệ");
        }
        return ResponseEntity.ok(this.cartItemService.convertToCartItemResponse(cartItem));
    }

    @GetMapping("{cartItemId}/increase")
    @ApiMessage("Tăng số lượng sản phẩm trong giỏ hàng thành công")
    public ResponseEntity<CartItemResponse> increaseCartItemQuantity(@Valid @PathVariable Long cartItemId)
            throws IdInvalidException {
        CartItem cartItem = this.cartItemService.increaseCartItemQuantity(cartItemId);
        if (cartItem == null) {
            throw new IdInvalidException("Cart item id không hợp lệ");
        }
        return ResponseEntity.ok(this.cartItemService.convertToCartItemResponse(cartItem));
    }

    @GetMapping("{cartItemId}/decrease")
    @ApiMessage("Giảm số lượng sản phẩm trong giỏ hàng thành công")
    public ResponseEntity<CartItemResponse> decreaseCartItemQuantity(@Valid @PathVariable Long cartItemId)
            throws IdInvalidException {
        CartItem cartItem = this.cartItemService.decreaseCartItemQuantity(cartItemId);
        if (cartItem == null) {
            throw new IdInvalidException("Cart item id không hợp lệ");
        }
        return ResponseEntity.ok(this.cartItemService.convertToCartItemResponse(cartItem));
    }
}
