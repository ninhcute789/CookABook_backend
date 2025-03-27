package NandK.CookABook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import NandK.CookABook.entity.Author;
import NandK.CookABook.entity.Book;
import NandK.CookABook.entity.Category;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    public Integer countByAuthor(Author author);

    // Tự động tạo truy vấn tìm sách theo categoryId bằng Derived Query
    public Page<Book> findByCategoriesContaining(Category category, Pageable pageable);

    public Page<Book> findByAuthor(Author author, Pageable pageable);
}
