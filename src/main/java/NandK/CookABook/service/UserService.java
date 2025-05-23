package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.user.UserCreationRequest;
import NandK.CookABook.dto.request.user.UserUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.user.UserCreationResponse;
import NandK.CookABook.dto.response.user.UserFoundResponse;
import NandK.CookABook.dto.response.user.UserUpdateResponse;
import NandK.CookABook.entity.Article;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.ArticleRepository;
import NandK.CookABook.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository; // final: khong the thay doi gia tri cua bien
    private final ArticleRepository articleRepository;
    private final CartService cartService;

    public UserService(UserRepository userRepository, ArticleRepository articleRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.cartService = cartService;
    }

    public boolean isUsernameExist(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User createUser(UserCreationRequest request) {
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setGender(request.getGender());
        user.setDob(request.getDob());
        user.setEmail(request.getEmail());
        user.setCart(this.cartService.createCart(user));
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
        response.setAvatar(user.getAvatar());
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
        response.setAvatar(user.getAvatar());
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
        List<UserFoundResponse> listUsers = users.getContent().stream().map(
                item -> new UserFoundResponse(
                        item.getId(),
                        item.getUsername(),
                        item.getName(),
                        item.getGender(),
                        item.getDob(),
                        item.getEmail(),
                        item.getAvatar(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()))
                .collect(Collectors.toList());
        result.setData(listUsers);
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
        if (request.getAvatar() != null && !request.getAvatar().isBlank()) {
            user.setAvatar(request.getAvatar());
        }
        return this.userRepository.save(user);
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    public void deleteUser(User user) {
        List<Article> articles = this.articleRepository.findByUser(user);
        this.articleRepository.deleteAll(articles);
        this.userRepository.delete(user);
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
