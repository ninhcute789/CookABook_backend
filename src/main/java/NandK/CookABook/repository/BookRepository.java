package NandK.CookABook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import NandK.CookABook.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    public Book findByTitle(String title);

    List<Book> findByAuthorsId(Long authorId);
}
