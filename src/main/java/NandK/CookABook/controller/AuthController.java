package NandK.CookABook.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.login.LoginResponse;
import NandK.CookABook.dto.password.ForgotPasswordRequest;
import NandK.CookABook.dto.user.UserLoginRequest;
import NandK.CookABook.entity.User;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.UserService;
import NandK.CookABook.utils.SecurityUtil;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @Value("${cookabook.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    private AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiMessage("Đăng nhập thành công")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // set thông tin người dùng vào SecurityContext (để sau này dùng)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // create token
        LoginResponse loginResponse = new LoginResponse();
        // lấy thông tin user
        User currentUser = this.userService.getUserByUsername(request.getUsername());
        if (currentUser != null) {
            LoginResponse.UserLoginInformation userLoginInformation = new LoginResponse.UserLoginInformation(
                    currentUser.getId(), currentUser.getUsername(), currentUser.getName());
            loginResponse.setUser(userLoginInformation);
        }
        // create access token
        String accessToken = this.securityUtil.createAccessToken(authentication.getName(), loginResponse.getUser());
        loginResponse.setAccessToken(accessToken);
        // create refresh token
        String refreshToken = this.securityUtil.createRefreshToken(request.getUsername(), loginResponse);
        // luu refresh token vào db
        this.userService.updateUserRefreshToken(refreshToken, request.getUsername());
        // set cookie
        ResponseCookie cookie = ResponseCookie
                .from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }

    @GetMapping("/account")
    @ApiMessage("Lấy thông tin tài khoản thành công")
    public ResponseEntity<LoginResponse.UserGetAccount> getAccount() {
        String username = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.getUserByUsername(username);
        LoginResponse.UserLoginInformation loginResponse = new LoginResponse.UserLoginInformation();
        LoginResponse.UserGetAccount userGetAccount = new LoginResponse.UserGetAccount();

        if (currentUser != null) {
            loginResponse.setId(currentUser.getId());
            loginResponse.setUsername(currentUser.getUsername());
            loginResponse.setName(currentUser.getName());
            userGetAccount.setUser(loginResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userGetAccount);
    }

    @GetMapping("/refresh")
    @ApiMessage("Refresh token thành công")
    public ResponseEntity<LoginResponse> getRefreshToken(
            @CookieValue(name = "refresh-token", defaultValue = "null") String refreshToken) throws IdInvalidException {
        // kiểm tra refresh token có tồn tại ở cookie không
        if (refreshToken.equals("null")) {
            throw new IdInvalidException("Refresh token không tồn tại trong cookie");
        }
        // kiểm tra tính hợp lệ của refresh token
        Jwt decodedRefreshToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String username = decodedRefreshToken.getSubject();
        // kiểm tra username có tồn tại trong db không
        User user = this.userService.getUserByRefreshTokenAndUsername(refreshToken, username);
        if (user == null) {
            throw new IdInvalidException("Refresh token không hợp lệ");
        }
        // tạo mới access token, set refresh token vào cookie

        LoginResponse loginResponse = new LoginResponse();
        // lấy thông tin user
        User currentUser = this.userService.getUserByUsername(username);
        if (currentUser != null) {
            LoginResponse.UserLoginInformation userLoginInformation = new LoginResponse.UserLoginInformation(
                    currentUser.getId(), currentUser.getUsername(), currentUser.getName());
            loginResponse.setUser(userLoginInformation);
        }
        // create access token
        String accessToken = this.securityUtil.createAccessToken(username, loginResponse.getUser());
        loginResponse.setAccessToken(accessToken);
        // create refresh token
        String newRefreshToken = this.securityUtil.createRefreshToken(username, loginResponse);
        // luu refresh token vào db
        this.userService.updateUserRefreshToken(newRefreshToken, username);
        // set cookie
        ResponseCookie cookie = ResponseCookie
                .from("refresh-token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/logout")
    @ApiMessage("Đăng xuất thành công")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        // xóa refresh token trong db
        String username = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        if (username.isBlank()) {
            throw new IdInvalidException("Access token không hợp lệ");
        }
        this.userService.updateUserRefreshToken(null, username);
        // xóa cookie
        ResponseCookie cookie = ResponseCookie
                .from("refresh-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(null);
    }

    // TODO: complete change password
    @PutMapping("/forgot-password")
    @ApiMessage("Đổi mật khẩu thành công")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        // boolean isEmailExist = this.userService.isEmailExist(request.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("Change password success");
    }
}
