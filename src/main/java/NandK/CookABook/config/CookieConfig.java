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
        serializer.setCookieName("JSESSIONID"); // Đặt tên cookie là JSESSIONID
        serializer.setCookiePath("/"); // Đặt đường dẫn cookie
        serializer.setSameSite("None"); // Cho phép gửi cookie trong các yêu cầu cross-origin
        serializer.setUseSecureCookie(true);
        serializer.setUseHttpOnlyCookie(true); // Chỉ cho phép cookie được truy cập qua HTTP, không qua JavaScript
        return serializer;
    }
}
