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
    User createUser(@RequestBody UserCreationRequest request){
        return userService.createUser(request);
    }

    @GetMapping
    List<User> getUsers(){
        return userService.getUsers();
    }
    
    @GetMapping("/{userId}") //get resource tu AIP, truyen tham so sau path /
    User getUser(@PathVariable String userId){ 
        return userService.getUser(userId);
    }

    
}
