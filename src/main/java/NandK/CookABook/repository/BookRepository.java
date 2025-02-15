package NandK.CookABook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import NandK.CookABook.entity.Book;
import org.springframework.stereotype.Repository;
// import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    // List<Book> findByAuthor(String author);
}
