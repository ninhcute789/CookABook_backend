package NandK.CookABook.repository;

import NandK.CookABook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    public User findByUsername(String username);

    public boolean existsByUsername(String username);

    public User findByRefreshTokenAndUsername(String refreshToken, String username);
}
