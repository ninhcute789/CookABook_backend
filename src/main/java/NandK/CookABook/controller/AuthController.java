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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.request.passwordResetToken.ForgotPasswordRequest;
import NandK.CookABook.dto.request.passwordResetToken.ResetPasswordRequest;
import NandK.CookABook.dto.request.passwordResetToken.VerifyTokenRequest;
import NandK.CookABook.dto.request.user.UserCreationRequest;
import NandK.CookABook.dto.request.user.UserLoginRequest;
import NandK.CookABook.dto.response.LoginResponse;
import NandK.CookABook.dto.response.user.UserCreationResponse;
import NandK.CookABook.entity.User;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.ForgotPasswordService;
import NandK.CookABook.service.UserService;
import NandK.CookABook.utils.SecurityUtil;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Value("${cookabook.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;
    private final ForgotPasswordService forgotPasswordService;
    private HttpSession session;

    private AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil,
            UserService userService,
            PasswordEncoder passwordEncoder,
            ForgotPasswordService forgotPasswordService,
            HttpSession session) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.forgotPasswordService = forgotPasswordService;
        this.session = session;
    }

    @PostMapping("/register")
    @ApiMessage("Đăng ký tài khoản thành công")
    public ResponseEntity<UserCreationResponse> register(@Valid @RequestBody UserCreationRequest request)
            throws IdInvalidException {
        boolean isUserNameExist = this.userService.isUsernameExist(request.getUsername());
        if (isUserNameExist) {
            throw new IdInvalidException(
                    "Username " + request.getUsername() + " đã tồn tại, vui lòng sử dụng username khác");
        }
        String hashPassword = this.passwordEncoder.encode(request.getPassword()); // ham encode tra ra String
        request.setPassword(hashPassword);
        User user = this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToUserCreationResponse(user));
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
                    currentUser.getId(), currentUser.getUsername(), currentUser.getName(),
                    currentUser.getCart().getId());
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
                .secure(true) // chỉ gửi cookie qua HTTPS
                .sameSite("None") // cho phép gửi cookie trong các yêu cầu cross-origin
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
            loginResponse.setCartId(currentUser.getCart().getId());
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
                    currentUser.getId(), currentUser.getUsername(), currentUser.getName(),
                    currentUser.getCart().getId());
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
                .secure(true) // chỉ gửi cookie qua HTTPS
                .sameSite("None") // cho phép gửi cookie trong các yêu cầu cross-origin
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
                .secure(true) // chỉ gửi cookie qua HTTPS
                .sameSite("None") // cho phép gửi cookie trong các yêu cầu cross-origin
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(null);
    }

    // Yêu cầu gửi mã xác nhận đến email
    @PostMapping("/forgot-password")
    @ApiMessage("Đã gửi mã xác nhận đến email")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request)
            throws IdInvalidException {
        if (!this.forgotPasswordService.isEmailExists(request.getEmail())) {
            throw new IdInvalidException("Email không tồn tại trong hệ thống");
        }
        session.removeAttribute("email");
        this.forgotPasswordService.sendResetCode(request.getEmail());
        session.setAttribute("email", request.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    // Yêu cầu đặt lại mật khẩu
    @PostMapping("/check-token")
    @ApiMessage("Mã xác nhận hợp lệ")
    public ResponseEntity<Void> checkToken(@Valid @RequestBody VerifyTokenRequest request)
            throws IdInvalidException {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            throw new IdInvalidException("Vui lòng yêu cầu mã xác nhận trước");
        }
        session.removeAttribute("token");
        // kiểm tra mã xác nhận có hợp lệ không
        if (!this.forgotPasswordService.isTokenValid(email, request.getToken())) {
            throw new IdInvalidException("Mã xác nhận không hợp lệ hoặc đã hết hạn");
        }
        session.setAttribute("token", request.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    // Đặt lại mật khẩu
    @PutMapping("/reset-password")
    @ApiMessage("Đặt lại mật khẩu thành công")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request)
            throws IdInvalidException {
        String email = (String) session.getAttribute("email");
        String token = (String) session.getAttribute("token");
        if (email == null || token == null) {
            throw new IdInvalidException("Vui lòng yêu cầu mã xác nhận trước");
        }
        // kiểm tra mã xác nhận có hợp lệ không
        if (!this.forgotPasswordService.isTokenValid(email, token)) {
            throw new IdInvalidException("Mã xác nhận không hợp lệ hoặc đã hết hạn");
        }
        // đặt lại mật khẩu
        String hashPassword = this.passwordEncoder.encode(request.getNewPassword());
        this.forgotPasswordService.resetPassword(email, token, hashPassword);
        // xóa thông tin trong session
        session.removeAttribute("email");
        session.removeAttribute("token");
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
