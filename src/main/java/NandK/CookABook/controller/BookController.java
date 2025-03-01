package NandK.CookABook.controller;

import NandK.CookABook.dto.book.BookCreationRequest;
import NandK.CookABook.entity.Book;
import NandK.CookABook.service.BookService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookCreationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bookService.createBook(request));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(this.bookService.getAllBooks());
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@Valid @PathVariable Long bookId) {
        Book book = this.bookService.getBookById(bookId);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }

    }

    // @PutMapping("/{bookId}")
    // public ResponseEntity<Book> updateBook(@PathVariable String bookId,
    // @RequestBody String request) {

    // return this.bookService.updateBook(bookId, request);
    // }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@Valid @PathVariable Long bookId) {
        this.bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }
}
