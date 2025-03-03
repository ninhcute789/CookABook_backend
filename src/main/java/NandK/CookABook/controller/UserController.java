package NandK.CookABook.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import NandK.CookABook.dto.pagination.ResultPagination;
import NandK.CookABook.dto.user.UserCreationRequest;
import NandK.CookABook.dto.user.UserUpdateRequest;
import NandK.CookABook.entity.User;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.UserService;
import NandK.CookABook.utils.annotation.ApiMessage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")

public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping
    @ApiMessage("Tạo người dùng thành công")
    public ResponseEntity<User> createUser(@RequestBody UserCreationRequest request) {
        String hashPassword = this.passwordEncoder.encode(request.getPassword()); // ham encode tra ra String
        request.setPassword(hashPassword);
        User user = this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user); // tra ve status 201 va user
    }

    @GetMapping
    @ApiMessage("Lấy danh sách người dùng thành công")
    public ResponseEntity<ResultPagination> getAllUsers(
            @Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers(spec, pageable));
    }

    @GetMapping("/{userId}")
    @ApiMessage("Lấy người dùng thành công")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else
            return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping
    @ApiMessage("Cập nhật người dùng thành công")
    public ResponseEntity<User> updateUserById(@RequestBody UserUpdateRequest request) {
        User user = this.userService.updateUserById(request);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{userId}")
    @ApiMessage("Xóa người dùng thành công")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) throws IdInvalidException {
        this.userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
