package NandK.CookABook.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.request.UserCreationRequest;
import NandK.CookABook.dto.request.UserUpdateRequest;
import NandK.CookABook.entity.User;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreationRequest request) {
        User user = this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user); // tra ve status 201 va user
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // List<User> user = this.userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = this.userService.getUser(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else
            return ResponseEntity.status(HttpStatus.OK).body(user);
        // return ResponseEntity.ok(user); //tra ve status 200 va user
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest request) {
        User user = this.userService.updateUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws IdInvalidException {
        if (userId >= 100) {
            throw new IdInvalidException("Id không hợp lệ");
        } // message duoc lay tu day
        this.userService.deleteUser(userId);
        return ResponseEntity.noContent().build(); // tra ve status 204
    }

}
