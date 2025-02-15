package NandK.CookABook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.request.UserCreationRequest;
import NandK.CookABook.entity.User;
import NandK.CookABook.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; 


@RequestMapping("/users")
@RestController

public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserCreationRequest request){
        return userService.createUser(request);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    
    @GetMapping("/{userId}") //get resource tu AIP, truyen tham so sau path /
    public User getUser(@PathVariable String userId){ 
        return userService.getUser(userId);
    }

    
}
