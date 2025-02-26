package NandK.CookABook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// disable security
// @SpringBootApplication(excludeName = {
// 		"org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
// 		"org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration"
// })

@SpringBootApplication
public class CookABookApplication {
	public static void main(String[] args) {
		SpringApplication.run(CookABookApplication.class, args);
	}

}
