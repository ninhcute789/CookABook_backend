package NandK.CookABook.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.request.UserCreationRequest;
import NandK.CookABook.dto.request.UserUpdateRequest;
import NandK.CookABook.entity.User;
import NandK.CookABook.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public User createUser(@RequestBody UserCreationRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    
    @GetMapping("/{userId}") //get resource tu AIP, truyen tham so sau path /
    public User getUser(@PathVariable Long userId){ 
        return userService.getUser(userId);
        }

    @PutMapping("/{userId}") //update resource tu API, truyen tham so sau path /
        public User updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
        }

    @DeleteMapping("/{userId}") //delete resource tu API, truyen tham so sau path /
        public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "User đã được xóa";
    }
    
}
