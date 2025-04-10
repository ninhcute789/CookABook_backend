package NandK.CookABook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import NandK.CookABook.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    public PasswordResetToken findByEmailAndToken(String email, String token);

    public void deleteByEmail(String email);
}
