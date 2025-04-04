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
        serializer.setSameSite("Lax"); // Chỉ định SameSite=Lax cho các yêu cầu không cross-origin
        return serializer;
    }
}
