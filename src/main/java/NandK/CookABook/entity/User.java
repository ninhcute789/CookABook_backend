package NandK.CookABook.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
//khai bao thu vien dung ham tu dong getter va setter
import lombok.Data;

@Entity
@Data //auto getter va setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  //tao ra ID ngau nhien khong trung lap
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String email;


}
