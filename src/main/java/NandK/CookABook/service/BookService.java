package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.author.AuthorCreationRequest;
import NandK.CookABook.dto.request.book.BookCreationRequest;
import NandK.CookABook.dto.request.book.BookUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.book.BookCreationResponse;
import NandK.CookABook.dto.response.book.BookFoundResponse;
import NandK.CookABook.dto.response.book.BookUpdateResponse;
import NandK.CookABook.entity.Author;
import NandK.CookABook.entity.Book;
import NandK.CookABook.entity.Category;
import NandK.CookABook.repository.AuthorRepository;
import NandK.CookABook.repository.BookRepository;
import NandK.CookABook.repository.CategoryRepository;

@Service
public class BookService {

    private final AuthorService authorService;

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, AuthorService authorService,
            CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.authorService = authorService;
        this.categoryRepository = categoryRepository;
    }

    public Book createBook(BookCreationRequest request) {
        Book book = new Book();
        // check author
        if (request.getAuthor() != null && request.getAuthor().getName() != null) {
            Author author = this.authorRepository.findByName(request.getAuthor().getName());
            if (author == null) {
                AuthorCreationRequest requestAuthor = new AuthorCreationRequest(request.getAuthor().getName());
                author = this.authorService.createAuthor(requestAuthor);
                book.setAuthor(author);
            } else {
                book.setAuthor(author);
            }
        } else {
            book.setAuthor(null);
        }
        // check category
        if (request.getCategories() != null) {
            List<Long> categoryIds = request.getCategories().stream().map(category -> category.getId())
                    .collect(Collectors.toList());
            List<Category> categories = this.categoryRepository.findByIdIn(categoryIds);
            book.setCategories(categories);
        } else {
            book.setCategories(null);
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
        book.setDiscountPrice((int) Math.round(
                book.getOriginalPrice() -
                        (book.getOriginalPrice() * book.getDiscountPercentage() / 100)));
        book.setStockQuantity(request.getStockQuantity());
        book.setAvailable(request.getAvailable());
        book.setDescription(request.getDescription());
        book.setCoverType(request.getCoverType());

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
        if (book.getCategories() != null) {
            List<BookCreationResponse.Category> categories = book.getCategories().stream().map(
                    item -> new BookCreationResponse.Category(item.getId(), item.getName()))
                    .collect(Collectors.toList());
            response.setCategories(categories);
        }
        response.setCreatedAt(book.getCreatedAt());
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
                                item.getAuthor().getName()) : null,
                        item.getCategories() != null ? item.getCategories().stream().map(
                                category -> new BookFoundResponse.Category(
                                        category.getId(),
                                        category.getName()))
                                .collect(Collectors.toList()) : null))
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
        if (book.getCategories() != null) {
            List<BookFoundResponse.Category> categories = book.getCategories().stream().map(
                    item -> new BookFoundResponse.Category(item.getId(), item.getName()))
                    .collect(Collectors.toList());
            response.setCategories(categories);
        }
        return response;
    }

    public Book updateBook(BookUpdateRequest request) {
        Book book = this.getBookById(request.getId());
        if (book != null) {
            // check author
            if (request.getAuthor() != null && request.getAuthor().getName() != null) {
                Author author = this.authorRepository.findByName(request.getAuthor().getName());
                if (author == null) {
                    AuthorCreationRequest requestAuthor = new AuthorCreationRequest(request.getAuthor().getName());
                    author = this.authorService.createAuthor(requestAuthor);
                    book.setAuthor(author);
                } else {
                    book.setAuthor(author);
                }
            } else {
                book.setAuthor(null);
            }
            // check category
            if (request.getCategories() != null) {
                List<Long> categoryIds = request.getCategories().stream().map(category -> category.getId())
                        .collect(Collectors.toList());
                List<Category> categories = this.categoryRepository.findByIdIn(categoryIds);
                book.setCategories(categories);
            } else {
                book.setCategories(null);
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
            if (request.getOriginalPrice() != null || request.getDiscountPercentage() != null) {
                book.setDiscountPrice((int) Math.round(
                        book.getOriginalPrice() -
                                (book.getOriginalPrice() * book.getDiscountPercentage() / 100)));
            } else if (request.getDiscountPercentage() == null) {
                book.setDiscountPrice(null);
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
        if (book.getCategories() != null) {
            List<BookUpdateResponse.Category> categories = book.getCategories().stream().map(
                    item -> new BookUpdateResponse.Category(item.getId(), item.getName()))
                    .collect(Collectors.toList());
            response.setCategories(categories);
        }
        return response;
    }

    public void deleteBookById(Long bookId) {
        // xóa tất cả các danh mục liên quan đến sách trước khi xóa sách
        Book book = this.getBookById(bookId);
        book.getCategories().forEach(category -> category.getBooks().remove(book));
        // xóa sách
        this.bookRepository.delete(book);
    }
}
