package NandK.CookABook.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import NandK.CookABook.entity.PasswordResetToken;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.PasswordResetTokenRepository;
import NandK.CookABook.repository.UserRepository;

@Service
public class ForgotPasswordService {

    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    public ForgotPasswordService(EmailService emailService,
            PasswordResetTokenRepository passwordResetTokenRepository,
            UserRepository userRepository) {
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
    }

    public boolean isEmailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 6); // Ví dụ mã 6 ký tự
    }

    public void sendResetCode(String email) {
        String code = generateCode();
        Instant expiry = Instant.now().plus(Duration.ofMinutes(10)); // Thời gian hết hạn 10 phút
        this.passwordResetTokenRepository.deleteByEmail(email); // Xóa mã cũ nếu có

        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setToken(code);
        token.setExpiry(expiry);
        this.passwordResetTokenRepository.save(token);

        this.emailService.sendVerificationCode(email, code);
    }

    public boolean isTokenValid(String email, String code) {
        PasswordResetToken token = this.passwordResetTokenRepository.findByEmailAndToken(email, code);
        if (token == null) {
            return false;
        }
        Instant now = Instant.now();
        return !now.isAfter(token.getExpiry());
    }

    public void resetPassword(String email, String code, String newPassword) {
        PasswordResetToken token = this.passwordResetTokenRepository.findByEmailAndToken(email, code);
        User user = this.userRepository.findByEmail(email);

        user.setPassword(newPassword); // Cập nhật mật khẩu mới
        this.userRepository.save(user); // Lưu thay đổi
        this.passwordResetTokenRepository.delete(token); // Xóa mã sau khi sử dụng
    }
}
