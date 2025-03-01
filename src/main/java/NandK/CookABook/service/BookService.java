package NandK.CookABook.service;

import NandK.CookABook.dto.book.BookCreationRequest;
import NandK.CookABook.entity.Book;
import NandK.CookABook.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(BookCreationRequest request) {
        Book book = new Book();

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());
        book.setImageUrl(request.getImageUrl());

        return this.bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return this.bookRepository.findAll();
    }

    public Book getBookById(Long bookId) {
        Optional<Book> book = this.bookRepository.findById(bookId);
        if (book.isPresent()) {
            return book.get();
        } else {
            return null;
        }
    }

    // public Book updateBook(Long bookId, BookCreationRequest request) {
    // Book book = this.getBookById(request.getId());
    // book.setTitle(request.getTitle());
    // book.setAuthor(request.getAuthor());
    // book.setDescription(request.getDescription());
    // book.setPrice(request.getPrice());
    // book.setStock(request.getStock());
    // book.setImageUrl(request.getImageUrl());

    // return this.bookRepository.save(book);
    // }

    public void deleteBook(Long id) {
        this.bookRepository.deleteById(id);
    }
}
