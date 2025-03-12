package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.UserCreationRequest;
import NandK.CookABook.dto.request.UserUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.UserCreationResponse;
import NandK.CookABook.dto.response.UserFoundResponse;
import NandK.CookABook.dto.response.UserUpdateResponse;
import NandK.CookABook.entity.Article;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.ArticleRepository;
import NandK.CookABook.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository; // final: khong the thay doi gia tri cua bien

    private final ArticleRepository articleRepository;

    public UserService(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public boolean isUsernameExist(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public User createUser(UserCreationRequest request) {
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setGender(request.getGender());
        user.setDob(request.getDob());
        user.setEmail(request.getEmail());

        return this.userRepository.save(user);

    }

    public UserCreationResponse convertToUserCreationResponse(User user) {
        UserCreationResponse response = new UserCreationResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setGender(user.getGender());
        response.setDob(user.getDob());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }

    public UserFoundResponse convertToUserFindByIdResponse(User user) {
        UserFoundResponse response = new UserFoundResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setGender(user.getGender());
        response.setDob(user.getDob());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public UserUpdateResponse convertToUserUpdateResponse(User user) {
        UserUpdateResponse response = new UserUpdateResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setGender(user.getGender());
        response.setDob(user.getDob());
        response.setEmail(user.getEmail());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public ResultPagination getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> users = this.userRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();
        // lay thong tin ve trang hien tai thong qua pageable tu client gui len
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        // lay tong so trang va tong so phan tu tu database
        meta.setTotalPages(users.getTotalPages());
        meta.setTotalElements(users.getTotalElements());
        // set thong tin tra ra client
        result.setMeta(meta);
        // loai bo thong tin nhay cam
        List<UserFoundResponse> listUser = users.getContent().stream().map(
                item -> new UserFoundResponse(
                        item.getId(),
                        item.getUsername(),
                        item.getName(),
                        item.getGender(),
                        item.getDob(),
                        item.getEmail(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()))
                .collect(Collectors.toList());
        result.setData(listUser);
        return result;
    }

    public User getUserById(Long userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
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
        User user = this.getUserById(request.getId());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }
        return this.userRepository.save(user);
    }

    public void deleteUserById(Long userId) {
        User user = this.getUserById(userId);
        List<Article> articles = this.articleRepository.findByUser(user);
        this.articleRepository.deleteAll(articles);
        this.userRepository.deleteById(userId);
    }

    public void updateUserRefreshToken(String refreshToken, String username) {
        User user = this.getUserByUsername(username);
        if (user != null) {
            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);
        }
    }

    public User getUserByRefreshTokenAndUsername(String refreshToken, String username) {
        return this.userRepository.findByRefreshTokenAndUsername(refreshToken, username);
    }
}
