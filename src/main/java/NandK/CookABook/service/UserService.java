package NandK.CookABook.service;

import java.util.List;
import org.springframework.stereotype.Service;
import NandK.CookABook.dto.request.UserCreationRequest;
import NandK.CookABook.dto.request.UserUpdateRequest;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository; //final: khong the thay doi gia tri cua bien
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserCreationRequest request){
    User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        user.setEmail(request.getEmail());

    return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        }

    public User updateUser(Long userId, UserUpdateRequest request) {
        User user = getUser(userId);
        
        if (request.getPassword() != null && !request.getPassword().isEmpty() && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty() && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty() && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName());
        }
        if (request.getDob() != null && !request.getDob().toString().isEmpty() && !request.getDob().toString().isBlank()) {
            user.setDob(request.getDob());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty() && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }
        
        return userRepository.save(user);
        }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
