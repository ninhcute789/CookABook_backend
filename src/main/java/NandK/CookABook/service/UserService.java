package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.pagination.Meta;
import NandK.CookABook.dto.pagination.ResultPagination;
import NandK.CookABook.dto.user.UserCreationRequest;
import NandK.CookABook.dto.user.UserUpdateRequest;
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

    public ResultPagination getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> users = this.userRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        Meta meta = new Meta();
        // lay thong tin ve trang hien tai thong qua pageable tu client gui len
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        // lay tong so trang va tong so phan tu tu database
        meta.setTotalPage(users.getTotalPages());
        meta.setTotalElement(users.getTotalElements());
        // set thong tin tra ra client
        result.setMeta(meta);
        result.setData(users.getContent());
        return result;
    }

    public User getUserById(Long userId) {
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

    public User updateUserById(UserUpdateRequest request) {
        User user = this.getUserById(request.getId());
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

    public void deleteUserById(Long userId) {
        this.userRepository.deleteById(userId);
    }
}
