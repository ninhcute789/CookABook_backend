package NandK.CookABook.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mã xác nhận đặt lại mật khẩu cho tài khoản CookABook");
        message.setText("Mã xác nhận của bạn là: " + code + "\nMã này sẽ hết hạn sau 10 phút.");
        javaMailSender.send(message);
    }
}
