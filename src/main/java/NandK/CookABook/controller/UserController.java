package NandK.CookABook.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.user.UserCreationRequest;
import NandK.CookABook.dto.user.UserUpdateRequest;
import NandK.CookABook.entity.User;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<User> createUser(@RequestBody UserCreationRequest request) {
        String hashPassword = this.passwordEncoder.encode(request.getPassword()); // ham encode tra ra String
        request.setPassword(hashPassword);
        User user = this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user); // tra ve status 201 va user
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = this.userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else
            return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest request) {
        User user = this.userService.updateUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws IdInvalidException {
        this.userService.deleteUser(userId);
        return ResponseEntity.noContent().build(); // tra ve status 204
    }

}
