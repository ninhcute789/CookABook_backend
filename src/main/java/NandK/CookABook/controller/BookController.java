package NandK.CookABook.controller;

import NandK.CookABook.dto.request.BookCreationRequest;
import NandK.CookABook.entity.Book;
import NandK.CookABook.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
   
    @PostMapping
    public Book createBook(@RequestBody BookCreationRequest request) {
        return bookService.createBook(request);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
    @GetMapping("/{bookId}") //get resource tu AIP, truyen tham so sau path /
    public Book getBook(@PathVariable String bookId){ 
        return bookService.getBook(bookId);
    }
}
