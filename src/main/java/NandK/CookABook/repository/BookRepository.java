package NandK.CookABook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import NandK.CookABook.entity.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
