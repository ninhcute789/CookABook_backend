package NandK.CookABook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class CookieConfig {
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID"); // Đặt tên cookie
        serializer.setCookiePath("/"); // Đặt đường dẫn cookie
        serializer.setSameSite("None"); // Cho phép cross-origin
        serializer.setUseSecureCookie(false); // Không yêu cầu HTTPS (đặt true nếu sử dụng HTTPS)
        serializer.setUseHttpOnlyCookie(true); // Đảm bảo cookie không thể truy cập được từ JavaScript
        return serializer;
    }
}
