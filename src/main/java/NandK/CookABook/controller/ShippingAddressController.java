package NandK.CookABook.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.request.shippingAddress.ShippingAddressCreationRequest;
import NandK.CookABook.dto.request.shippingAddress.ShippingAddressUpdateRequest;
import NandK.CookABook.dto.response.shippingAddress.ShippingAddressResponse;
import NandK.CookABook.entity.ShippingAddress;
import NandK.CookABook.entity.User;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.repository.ShippingAddressRepository;
import NandK.CookABook.service.ShippingAddressService;
import NandK.CookABook.service.UserService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shipping-addresses")
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;
    private final ShippingAddressRepository shippingAddressRepository;
    private final UserService userService;

    public ShippingAddressController(ShippingAddressService shippingAddressService,
            ShippingAddressRepository shippingAddressRepository, UserService userService) {
        this.shippingAddressService = shippingAddressService;
        this.shippingAddressRepository = shippingAddressRepository;
        this.userService = userService;
    }

    @PostMapping
    @ApiMessage("Tạo địa chỉ giao hàng thành công")
    public ResponseEntity<ShippingAddressResponse> createShippingAddress(
            @Valid @RequestBody ShippingAddressCreationRequest request) throws IdInvalidException {
        User user = this.userService.getUserById(request.getUserId());
        if (user == null) {
            throw new IdInvalidException("Id người dùng = " + request.getUserId() + " không hợp lệ");
        }
        Integer countShippingAddress = this.shippingAddressRepository.countByUser(user);
        if (countShippingAddress >= 6) {
            throw new IdInvalidException("Người dùng đã có 6 địa chỉ giao hàng, không thể tạo thêm");
        }
        ShippingAddress shippingAddress = this.shippingAddressService.createShippingAddress(request, user);
        return ResponseEntity.ok(this.shippingAddressService.convertToShippingAddressResponse(shippingAddress));
    }

    @GetMapping("/{shippingAddressId}")
    @ApiMessage("Lấy thông tin địa chỉ giao hàng thành công")
    public ResponseEntity<ShippingAddressResponse> getShippingAddressById(@PathVariable Long shippingAddressId)
            throws IdInvalidException {
        ShippingAddress shippingAddress = this.shippingAddressService.getShippingAddressById(shippingAddressId);
        if (shippingAddress == null) {
            throw new IdInvalidException("Id địa chỉ giao hàng = " + shippingAddressId + " không hợp lệ");
        }
        return ResponseEntity.ok(this.shippingAddressService.convertToShippingAddressResponse(shippingAddress));
    }

    @GetMapping("/all-by-user-id/{userId}")
    @ApiMessage("Lấy danh sách địa chỉ giao hàng theo userId thành công")
    public ResponseEntity<List<ShippingAddressResponse>> getShippingAddressesByUserId(
            @PathVariable Long userId) throws IdInvalidException {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            throw new IdInvalidException("Id người dùng = " + userId + " không hợp lệ");
        }
        return ResponseEntity.ok(this.shippingAddressService.getShippingAddressesByUser(user));
    }

    @GetMapping("/default-by-user-id/{userId}")
    @ApiMessage("Lấy địa chỉ giao hàng mặc định theo userId thành công")
    public ResponseEntity<ShippingAddressResponse> getDefaultShippingAddressByUserId(
            @PathVariable Long userId) throws IdInvalidException {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            throw new IdInvalidException("Id người dùng = " + userId + " không hợp lệ");
        }
        ShippingAddress shippingAddress = this.shippingAddressService.getDefaultShippingAddressByUser(user);
        if (shippingAddress == null) {
            throw new IdInvalidException("Người dùng chưa có địa chỉ giao hàng mặc định");
        }
        return ResponseEntity.ok(this.shippingAddressService.convertToShippingAddressResponse(shippingAddress));
    }

    @PutMapping
    @ApiMessage("Cập nhật địa chỉ giao hàng thành công")
    public ResponseEntity<ShippingAddressResponse> updateShippingAddress(
            @Valid @RequestBody ShippingAddressUpdateRequest request) throws IdInvalidException {
        ShippingAddress shippingAddress = this.shippingAddressService.updateShippingAddress(request);
        if (shippingAddress == null) {
            throw new IdInvalidException("Id địa chỉ giao hàng = " + request.getId() + " không hợp lệ");
        }
        return ResponseEntity.ok(this.shippingAddressService.convertToShippingAddressResponse(shippingAddress));
    }

    @DeleteMapping("/{shippingAddressId}")
    @ApiMessage("Xóa địa chỉ giao hàng thành công")
    public ResponseEntity<Void> deleteShippingAddress(@PathVariable Long shippingAddressId)
            throws IdInvalidException {
        ShippingAddress shippingAddress = this.shippingAddressService.getShippingAddressById(shippingAddressId);
        if (shippingAddress == null) {
            throw new IdInvalidException("Id địa chỉ giao hàng = " + shippingAddressId + " không hợp lệ");
        }
        if (shippingAddress.getDefaultAddress()) {
            throw new IdInvalidException("Không thể xóa địa chỉ giao hàng mặc định");
        }
        this.shippingAddressService.deleteShippingAddress(shippingAddress);
        return ResponseEntity.ok(null);
    }
}
