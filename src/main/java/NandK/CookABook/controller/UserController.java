package NandK.CookABook.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import NandK.CookABook.dto.request.user.UserCreationRequest;
import NandK.CookABook.dto.request.user.UserPasswordUpdateRequest;
import NandK.CookABook.dto.request.user.UserUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.user.UserCreationResponse;
import NandK.CookABook.dto.response.user.UserFoundResponse;
import NandK.CookABook.dto.response.user.UserUpdateResponse;
import NandK.CookABook.entity.User;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.ArticleService;
import NandK.CookABook.service.OrderService;
import NandK.CookABook.service.UserService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/users")

public class UserController {

    private final UserService userService;
    private final ArticleService articleService;
    private final OrderService orderService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, ArticleService articleService,
            OrderService orderService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.articleService = articleService;
        this.orderService = orderService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @ApiMessage("Tạo người dùng thành công")
    public ResponseEntity<UserCreationResponse> createUser(@Valid @RequestBody UserCreationRequest request)
            throws IdInvalidException {
        boolean isUserNameExist = this.userService.isUsernameExist(request.getUsername());
        if (isUserNameExist) {
            throw new IdInvalidException(
                    "Username " + request.getUsername() + " đã tồn tại, vui lòng sử dụng username khác");
        }
        boolean isEmailExist = this.userService.isEmailExist(request.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email " + request.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
        }
        String hashPassword = this.passwordEncoder.encode(request.getPassword()); // ham encode tra ra String
        request.setPassword(hashPassword);
        User user = this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToUserCreationResponse(user));
    }

    @GetMapping
    @ApiMessage("Lấy danh sách người dùng thành công")
    public ResponseEntity<ResultPagination> getAllUsers(
            @Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers(spec, pageable));
    }

    @GetMapping("/{userId}/articles")
    @ApiMessage("Lấy danh sách bài viết theo userId thành công")
    public ResponseEntity<ResultPagination> getArticlesByUserId(
            @PathVariable Long userId, Pageable pageable) throws IdInvalidException {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            throw new IdInvalidException("User với Id = " + userId + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.articleService.getAllArticlesByUser(user, pageable));
    }

    @GetMapping("/{userId}/orders")
    @ApiMessage("Lấy danh sách đơn hàng theo userId thành công")
    public ResponseEntity<ResultPagination> getOrdersByUserId(
            @PathVariable Long userId, Pageable pageable) throws IdInvalidException {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            throw new IdInvalidException("User với Id = " + userId + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.getAllOrdersByUser(user, pageable));
    }

    @GetMapping("/{userId}")
    @ApiMessage("Lấy người dùng thành công")
    public ResponseEntity<UserFoundResponse> getUserById(@PathVariable Long userId)
            throws IdInvalidException {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            throw new IdInvalidException("User với Id = " + userId + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToUserFindByIdResponse(user));
    }

    @PutMapping
    @ApiMessage("Cập nhật người dùng thành công")
    public ResponseEntity<UserUpdateResponse> updateUser(@Valid @RequestBody UserUpdateRequest request)
            throws IdInvalidException {
        User user = this.userService.getUserById(request.getId());
        if (user == null) {
            throw new IdInvalidException("User với Id = " + request.getId() + " không tồn tại");
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String hashPassword = this.passwordEncoder.encode(request.getPassword());
            request.setPassword(hashPassword);
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            boolean isEmailExist = this.userService.isEmailExist(request.getEmail());
            if (isEmailExist) {
                throw new IdInvalidException(
                        "Email " + request.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
            }
        }
        user = this.userService.updateUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToUserUpdateResponse(user));
    }

    @PutMapping("/update-password")
    @ApiMessage("Cập nhật mật khẩu người dùng thành công")
    public ResponseEntity<Void> updateUserPassword(@Valid @RequestBody UserPasswordUpdateRequest request)
            throws IdInvalidException {
        if (this.userService.isUsernameExist(request.getUsername())) {
            User user = this.userService.getUserByUsername(request.getUsername());
            boolean isPasswordValid = this.passwordEncoder.matches(request.getOldPassword(), user.getPassword());
            if (!isPasswordValid) {
                throw new IdInvalidException("Mật khẩu cũ không đúng, vui lòng kiểm tra lại");
            }
            boolean isNewPasswordValid = this.passwordEncoder.matches(request.getNewPassword(), user.getPassword());
            if (isNewPasswordValid) {
                throw new IdInvalidException("Mật khẩu mới không được giống với mật khẩu cũ");
            }
            String hashPassword = this.passwordEncoder.encode(request.getNewPassword());
            user.setPassword(hashPassword);
            this.userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(null); // password updated successfully
        } else {
            throw new IdInvalidException("Username " + request.getUsername() + " không tồn tại, vui lòng kiểm tra lại");
        }
    }

    @DeleteMapping("/{userId}")
    @ApiMessage("Xóa người dùng thành công")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) throws IdInvalidException {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            throw new IdInvalidException("User với Id = " + userId + " không tồn tại");
        }
        this.userService.deleteUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
