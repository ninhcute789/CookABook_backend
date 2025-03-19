package NandK.CookABook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import NandK.CookABook.entity.Author;
import NandK.CookABook.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    public List<Book> findByAuthor(Author author);

    public Integer countByAuthorId(Long authorId);
}
