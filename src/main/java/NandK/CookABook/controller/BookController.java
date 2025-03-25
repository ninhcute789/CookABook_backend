package NandK.CookABook.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import NandK.CookABook.dto.request.book.BookCreationRequest;
import NandK.CookABook.dto.request.book.BookUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.book.BookCreationResponse;
import NandK.CookABook.dto.response.book.BookFoundResponse;
import NandK.CookABook.dto.response.book.BookUpdateResponse;
import NandK.CookABook.entity.Author;
import NandK.CookABook.entity.Book;
import NandK.CookABook.entity.Category;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.AuthorService;
import NandK.CookABook.service.BookService;
import NandK.CookABook.service.CategoryService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    private final CategoryService categoryService;

    private final AuthorService authorService;

    public BookController(BookService bookService, CategoryService categoryService, AuthorService authorService) {
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.bookService = bookService;
    }

    @PostMapping
    @ApiMessage("Tạo sách thành công")
    public ResponseEntity<BookCreationResponse> createBook(@Valid @RequestBody BookCreationRequest request) {
        Book book = this.bookService.createBook(request);
        return ResponseEntity.ok(this.bookService.convertToBookCreationResponse(book));
    }

    @GetMapping("/all")
    @ApiMessage("Lấy danh sách sách thành công")
    public ResponseEntity<ResultPagination> getAllBooks(@Filter Specification<Book> spec, Pageable pageable) {
        return ResponseEntity.ok(this.bookService.getAllBooks(spec, pageable));
    }

    @GetMapping("/preview")
    @ApiMessage("Lấy danh sách sách theo trang thành công")
    public ResponseEntity<ResultPagination> getBooksPreview(@Filter Specification<Book> spec, Pageable pageable) {
        return ResponseEntity.ok(this.bookService.getBooksPreview(spec, pageable));
    }

    @GetMapping("/all-by-author/{authorId}")
    @ApiMessage("Lấy danh sách sách theo tác giả thành công")
    public ResponseEntity<ResultPagination> getAllBooksByAuthor(
            @PathVariable Long authorId, Pageable pageable)
            throws IdInvalidException {
        Author author = this.authorService.getAuthorById(authorId);
        if (author == null) {
            throw new IdInvalidException("Không tìm thấy tác giả với id: " + authorId);
        }
        return ResponseEntity.ok(this.bookService.getAllBooksByAuthor(authorId, pageable));
    }

    @GetMapping("/all-by-category/{categoryId}")
    @ApiMessage("Lấy danh sách sách theo danh mục thành công")
    public ResponseEntity<ResultPagination> getAllBooksByCategory(
            @PathVariable Long categoryId, Pageable pageable)
            throws IdInvalidException {
        Category category = this.categoryService.getCategoryById(categoryId);
        if (category == null) {
            throw new IdInvalidException("Không tìm thấy danh mục với id: " + categoryId);
        }
        return ResponseEntity.ok(this.bookService.getAllBooksByCategory(categoryId, pageable));
    }

    @GetMapping("/{bookId}")
    @ApiMessage("Lấy sách thành công")
    public ResponseEntity<BookFoundResponse> getBookById(@Valid @PathVariable Long bookId) throws IdInvalidException {
        Book book = this.bookService.getBookById(bookId);
        if (book == null) {
            throw new IdInvalidException("Không tìm thấy sách với id: " + bookId);
        }
        return ResponseEntity.ok(this.bookService.convertToBookFoundResponse(book));
    }

    @PutMapping
    @ApiMessage("Cập nhật sách thành công")
    public ResponseEntity<BookUpdateResponse> updateBook(@Valid @RequestBody BookUpdateRequest request)
            throws IdInvalidException {
        Book book = this.bookService.updateBook(request);
        if (book == null) {
            throw new IdInvalidException("Không tìm thấy sách với id: " + request.getId());
        }
        return ResponseEntity.ok(this.bookService.convertToBookUpdateResponse(book));
    }

    @DeleteMapping("/{bookId}")
    @ApiMessage("Xóa sách thành công")
    public ResponseEntity<Void> deleteBookById(@Valid @PathVariable Long bookId) throws IdInvalidException {
        Book book = this.bookService.getBookById(bookId);
        if (book == null) {
            throw new IdInvalidException("Không tìm thấy sách với id: " + bookId);
        }
        this.bookService.deleteBook(book);
        return ResponseEntity.ok(null);
    }

}
