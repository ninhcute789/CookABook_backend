package NandK.CookABook.controller;

import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/v1/carts")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    public CartController(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/quantity/{cartId}")
    @ApiMessage("Lấy số lượng sản phẩm trong giỏ hàng thành công")
    public ResponseEntity<Integer> getTotalQuantity(@Valid @PathVariable Long cartId) throws IdInvalidException {
        Cart cart = this.cartService.getCartById(cartId);
        if (cart == null) {
            throw new IdInvalidException("Cart id không hợp lệ");
        }
        return ResponseEntity.ok(cart.getTotalQuantity());
    }

    @GetMapping("{cartId}")
    @ApiMessage("Lấy giỏ hàng thành công")
    public ResponseEntity<CartPreviewResponse> getCartById(@Valid @PathVariable Long cartId)
            throws IdInvalidException {
        Cart cart = this.cartService.getCartById(cartId);
        if (cart == null) {
            throw new IdInvalidException("Cart id không hợp lệ");
        }
        return ResponseEntity.ok(this.cartService.convertToCartPreviewResponse(cart));
    }

    @PostMapping("/add")
    @ApiMessage("Thêm sản phẩm vào giỏ hàng thành công")
    public ResponseEntity<CartItemResponse> addToCart(@Valid @RequestBody AddToCartRequest request)
            throws IdInvalidException {
        CartItem cartItem = this.cartService.addToCart(request);
        if (cartItem == null) {
            throw new IdInvalidException("Cart id hoặc book id không hợp lệ");
        }
        return ResponseEntity.ok(this.cartItemService.convertToCartItemResponse(cartItem));
    }

}
