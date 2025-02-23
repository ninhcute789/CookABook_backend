package NandK.CookABook.controller;

import NandK.CookABook.dto.request.BookCreationRequest;
import NandK.CookABook.entity.Book;
import NandK.CookABook.service.BookService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    
    private final BookService bookService;
    
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @PostMapping
    public Book createBook(@RequestBody BookCreationRequest request) {
        return bookService.createBook(request);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
    @GetMapping("/{bookId}") //get resource tu AIP, truyen tham so sau path /
    public Book getBook(@PathVariable Long bookId){ 
        return bookService.getBook(bookId);
    }
}
