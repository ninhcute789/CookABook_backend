package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import NandK.CookABook.dto.request.UserCreationRequest;
import NandK.CookABook.dto.request.UserUpdateRequest;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository; // final: khong the thay doi gia tri cua bien

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserCreationRequest request) {
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        user.setEmail(request.getEmail());

        return this.userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User getUser(Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    public User getUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    public User updateUser(UserUpdateRequest request) {
        User user = this.getUser(request.getId());
        if (user != null) {
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                user.setPassword(request.getPassword());
            }
            if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
                user.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null && !request.getLastName().isBlank()) {
                user.setLastName(request.getLastName());
            }
            if (request.getDob() != null && !request.getDob().toString().isBlank()) {
                user.setDob(request.getDob());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                user.setEmail(request.getEmail());
            }
            user = this.userRepository.save(user);
        }
        return user;
    }

    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }
}
