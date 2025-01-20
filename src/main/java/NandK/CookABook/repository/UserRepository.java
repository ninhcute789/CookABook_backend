package NandK.CookABook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import NandK.CookABook.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
