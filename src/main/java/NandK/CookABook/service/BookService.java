package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.BookCreationRequest;
import NandK.CookABook.dto.request.BookUpdateRequest;
import NandK.CookABook.dto.response.BookCreationResponse;
import NandK.CookABook.dto.response.BookFoundResponse;
import NandK.CookABook.dto.response.BookUpdateResponse;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.entity.Author;
import NandK.CookABook.entity.Book;
import NandK.CookABook.repository.AuthorRepository;
import NandK.CookABook.repository.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Book createBook(BookCreationRequest request) {
        Book book = new Book();
        // check author
        if (request.getAuthor() != null && request.getAuthor().getId() != null) {
            Optional<Author> authorOptional = this.authorRepository.findById(request.getAuthor().getId());
            book.setAuthor(authorOptional.isPresent() ? authorOptional.get() : null);
        } else {
            book.setAuthor(null);
        }
        book.setTitle(request.getTitle());
        book.setPublisher(request.getPublisher());
        book.setPublishYear(request.getPublishYear());
        book.setSize(request.getSize());
        book.setNumberOfPages(request.getNumberOfPages());
        book.setWeight(request.getWeight());
        book.setLanguage(request.getLanguage());
        book.setImageURL(request.getImageURL());
        book.setOriginalPrice(request.getOriginalPrice());
        book.setDiscountPercentage(request.getDiscountPercentage());
        if (book.getDiscountPercentage() != null) {
            book.setDiscountPrice(
                    book.getOriginalPrice() - (book.getOriginalPrice() * book.getDiscountPercentage() / 100));
        }
        book.setStockQuantity(request.getStockQuantity());
        book.setAvailable(request.getAvailable());
        book.setDescription(request.getDescription());
        book.setCoverType(request.getCoverType());
        // book.setCategory(request.getCategory());

        return this.bookRepository.save(book);
    }

    public BookCreationResponse convertToBookCreationResponse(Book book) {
        BookCreationResponse response = new BookCreationResponse();
        BookCreationResponse.Author author = new BookCreationResponse.Author();

        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setPublisher(book.getPublisher());
        response.setPublishYear(book.getPublishYear());
        response.setSize(book.getSize());
        response.setNumberOfPages(book.getNumberOfPages());
        response.setWeight(book.getWeight());
        response.setLanguage(book.getLanguage());
        response.setImageURL(book.getImageURL());
        response.setOriginalPrice(book.getOriginalPrice());
        response.setDiscountPercentage(book.getDiscountPercentage());
        response.setDiscountPrice(book.getDiscountPrice());
        response.setStockQuantity(book.getStockQuantity());
        response.setAvailable(book.getAvailable());
        response.setDescription(book.getDescription());
        response.setCoverType(book.getCoverType());
        if (book.getAuthor() != null) {
            author.setId(book.getAuthor().getId());
            author.setName(book.getAuthor().getName());
            response.setAuthor(author);
        }
        response.setCreatedAt(book.getCreatedAt());
        // response.setCategory(book.getCategory());

        return response;
    }

    public ResultPagination getAllBooks(Specification<Book> spec, Pageable pageable) {
        Page<Book> books = this.bookRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(books.getTotalPages());
        meta.setTotalElements(books.getTotalElements());

        result.setMeta(meta);

        List<BookFoundResponse> listBooks = books.getContent().stream().map(
                item -> new BookFoundResponse(
                        item.getId(),
                        item.getTitle(),
                        item.getPublisher(),
                        item.getPublishYear(),
                        item.getSize(),
                        item.getNumberOfPages(),
                        item.getWeight(),
                        item.getLanguage(),
                        item.getImageURL(),
                        item.getOriginalPrice(),
                        item.getDiscountPercentage(),
                        item.getDiscountPrice(),
                        item.getStockQuantity(),
                        item.getAvailable(),
                        item.getDescription(),
                        item.getCoverType(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getAuthor() != null ? new BookFoundResponse.Author(
                                item.getAuthor().getId(),
                                item.getAuthor().getName()) : null))
                .collect(Collectors.toList());
        result.setData(listBooks);
        return result;
    }

    public Book getBookById(Long bookId) {
        Optional<Book> book = this.bookRepository.findById(bookId);
        if (book.isPresent()) {
            return book.get();
        } else {
            return null;
        }
    }

    public BookFoundResponse convertToBookFoundResponse(Book book) {
        BookFoundResponse response = new BookFoundResponse();
        BookFoundResponse.Author author = new BookFoundResponse.Author();

        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setPublisher(book.getPublisher());
        response.setPublishYear(book.getPublishYear());
        response.setSize(book.getSize());
        response.setNumberOfPages(book.getNumberOfPages());
        response.setWeight(book.getWeight());
        response.setLanguage(book.getLanguage());
        response.setImageURL(book.getImageURL());
        response.setOriginalPrice(book.getOriginalPrice());
        response.setDiscountPercentage(book.getDiscountPercentage());
        response.setDiscountPrice(book.getDiscountPrice());
        response.setStockQuantity(book.getStockQuantity());
        response.setAvailable(book.getAvailable());
        response.setDescription(book.getDescription());
        response.setCoverType(book.getCoverType());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        if (book.getAuthor() != null) {
            author.setId(book.getAuthor().getId());
            author.setName(book.getAuthor().getName());
            response.setAuthor(author);
        }
        // response.setCategory(book.getCategory());

        return response;
    }

    public Book updateBook(BookUpdateRequest request) {
        Book book = this.getBookById(request.getId());
        if (book != null) {
            // check author
            if (request.getAuthor() != null && request.getAuthor().getId() != null) {
                Optional<Author> authorOptional = this.authorRepository.findById(request.getAuthor().getId());
                book.setAuthor(authorOptional.isPresent() ? authorOptional.get() : null);
            } else {
                book.setAuthor(null);
            }
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                book.setTitle(request.getTitle());
            }
            if (request.getPublisher() != null && !request.getPublisher().isBlank()) {
                book.setPublisher(request.getPublisher());
            }
            if (request.getPublishYear() != null) {
                book.setPublishYear(request.getPublishYear());
            }
            if (request.getSize() != null && !request.getSize().isBlank()) {
                book.setSize(request.getSize());
            }
            if (request.getNumberOfPages() != null) {
                book.setNumberOfPages(request.getNumberOfPages());
            }
            if (request.getWeight() != null) {
                book.setWeight(request.getWeight());
            }
            if (request.getLanguage() != null && !request.getLanguage().isBlank()) {
                book.setLanguage(request.getLanguage());
            }
            if (request.getImageURL() != null && !request.getImageURL().isBlank()) {
                book.setImageURL(request.getImageURL());
            }
            if (request.getOriginalPrice() != null) {
                book.setOriginalPrice(request.getOriginalPrice());
            }
            if (request.getDiscountPercentage() != null) {
                book.setDiscountPercentage(request.getDiscountPercentage());
            }
            if (book.getDiscountPercentage() != null) {
                book.setDiscountPrice(
                        book.getOriginalPrice() - (book.getOriginalPrice() * book.getDiscountPercentage() / 100));
            }
            if (request.getStockQuantity() != null) {
                book.setStockQuantity(request.getStockQuantity());
            }
            if (request.getAvailable() != null) {
                book.setAvailable(request.getAvailable());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                book.setDescription(request.getDescription());
            }
            if (request.getCoverType() != null) {
                book.setCoverType(request.getCoverType());
            }
            // book.setCategory(request.getCategory());

            return this.bookRepository.save(book);
        } else {
            return null;
        }
    }

    public BookUpdateResponse convertToBookUpdateResponse(Book book) {
        BookUpdateResponse response = new BookUpdateResponse();
        BookUpdateResponse.Author author = new BookUpdateResponse.Author();

        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setPublisher(book.getPublisher());
        response.setPublishYear(book.getPublishYear());
        response.setSize(book.getSize());
        response.setNumberOfPages(book.getNumberOfPages());
        response.setWeight(book.getWeight());
        response.setLanguage(book.getLanguage());
        response.setImageURL(book.getImageURL());
        response.setOriginalPrice(book.getOriginalPrice());
        response.setDiscountPercentage(book.getDiscountPercentage());
        response.setDiscountPrice(book.getDiscountPrice());
        response.setStockQuantity(book.getStockQuantity());
        response.setAvailable(book.getAvailable());
        response.setDescription(book.getDescription());
        response.setCoverType(book.getCoverType());
        response.setUpdatedAt(book.getUpdatedAt());
        if (book.getAuthor() != null) {
            author.setId(book.getAuthor().getId());
            author.setName(book.getAuthor().getName());
            response.setAuthor(author);
        }
        // response.setCategory(book.getCategory());

        return response;
    }

    public void deleteBookById(Long bookId) {
        this.bookRepository.deleteById(bookId);
    }
}
