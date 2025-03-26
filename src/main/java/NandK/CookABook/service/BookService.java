package NandK.CookABook.service;

import java.util.Collections;
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
import NandK.CookABook.dto.response.book.BookPreviewResponse;
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
        if (request.getAuthor() != null && request.getAuthor().getName() != null
                && !request.getAuthor().getName().isEmpty()) {
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
        if (request.getCategories() != null && !request.getCategories().isEmpty()) {
            List<Long> categoryIds = request.getCategories().stream().map(category -> category.getId())
                    .collect(Collectors.toList());
            List<Category> categories = this.categoryRepository.findByIdIn(categoryIds);
            if (!categories.isEmpty()) {
                book.setCategories(categories);
            } else {
                // Handle the case when no categories are found
                book.setCategories(Collections.emptyList());
            }
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
        book.setFinalPrice((int) Math.round(
                book.getOriginalPrice() -
                        (book.getOriginalPrice() * book.getDiscountPercentage() / 100)));
        book.setStockQuantity(request.getStockQuantity());
        book.setAvailable(request.getAvailable());
        book.setOfficial(request.getOfficial());
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
        response.setFinalPrice(book.getFinalPrice());
        response.setStockQuantity(book.getStockQuantity());
        response.setAvailable(book.getAvailable());
        response.setOfficial(book.getOfficial());
        response.setDescription(book.getDescription());
        response.setCoverType(book.getCoverType());
        if (book.getAuthor() != null) {
            author.setId(book.getAuthor().getId());
            author.setName(book.getAuthor().getName());
            response.setAuthor(author);
        }
        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
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
                        item.getFinalPrice(),
                        item.getStockQuantity(),
                        item.getAvailable(),
                        item.getOfficial(),
                        item.getDescription(),
                        item.getCoverType(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        // Dùng Optional để tránh NullPointerException cho Author
                        Optional.ofNullable(item.getAuthor())
                                .map(author -> new BookFoundResponse.Author(author.getId(), author.getName()))
                                .orElse(null),
                        // Dùng kiểm tra null hoặc rỗng với category list
                        Optional.ofNullable(item.getCategories())
                                .map(categories -> categories.stream()
                                        .map(category -> new BookFoundResponse.Category(category.getId(),
                                                category.getName()))
                                        .collect(Collectors.toList()))
                                .orElse(Collections.emptyList())))
                .collect(Collectors.toList());
        result.setData(listBooks);
        return result;
    }

    public List<BookPreviewResponse> convertToBookPreviewResponses(List<Book> books) {
        return books.stream().map(
                item -> new BookPreviewResponse(
                        item.getId(),
                        item.getTitle(),
                        Optional.ofNullable(item.getAuthor())
                                .map(author -> author.getName())
                                .orElse(null),
                        item.getImageURL(),
                        item.getOriginalPrice(),
                        item.getDiscountPercentage(),
                        item.getFinalPrice(),
                        item.getAvailable(),
                        item.getOfficial()))
                .collect(Collectors.toList());
    }

    public ResultPagination getBooksPreview(Specification<Book> spec, Pageable pageable) {
        Page<Book> books = this.bookRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(books.getTotalPages());
        meta.setTotalElements(books.getTotalElements());

        result.setMeta(meta);

        List<BookPreviewResponse> listBooks = this.convertToBookPreviewResponses(books.getContent());
        result.setData(listBooks);
        return result;
    }

    public ResultPagination getAllBooksByCategory(Long categoryId, Pageable pageable) {
        Page<Book> books = this.bookRepository.findByCategories_Id(categoryId, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(books.getTotalPages());
        meta.setTotalElements(books.getTotalElements());

        result.setMeta(meta);

        List<BookPreviewResponse> listBooks = this.convertToBookPreviewResponses(books.getContent());
        result.setData(listBooks);
        return result;
    }

    public ResultPagination getAllBooksByAuthor(Long authorId, Pageable pageable) {
        Page<Book> books = this.bookRepository.findByAuthorId(authorId, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(books.getTotalPages());
        meta.setTotalElements(books.getTotalElements());

        result.setMeta(meta);

        List<BookPreviewResponse> listBooks = this.convertToBookPreviewResponses(books.getContent());
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
        response.setFinalPrice(book.getFinalPrice());
        response.setStockQuantity(book.getStockQuantity());
        response.setAvailable(book.getAvailable());
        response.setOfficial(book.getOfficial());
        response.setDescription(book.getDescription());
        response.setCoverType(book.getCoverType());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        if (book.getAuthor() != null) {
            author.setId(book.getAuthor().getId());
            author.setName(book.getAuthor().getName());
            response.setAuthor(author);
        }
        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
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
            if (request.getAuthor() != null && request.getAuthor().getName() != null
                    && !request.getAuthor().getName().isEmpty()) {
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
            if (request.getCategories() != null && !request.getCategories().isEmpty()) {
                List<Long> categoryIds = request.getCategories().stream().map(category -> category.getId())
                        .collect(Collectors.toList());
                List<Category> categories = this.categoryRepository.findByIdIn(categoryIds);
                if (!categories.isEmpty()) {
                    book.setCategories(categories);
                } else {
                    // Handle the case when no categories are found
                    book.setCategories(Collections.emptyList());
                }
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
                book.setFinalPrice((int) Math.round(
                        book.getOriginalPrice() -
                                (book.getOriginalPrice() * book.getDiscountPercentage() / 100)));
            } else if (request.getDiscountPercentage() == null) {
                book.setFinalPrice(null);
            }
            if (request.getStockQuantity() != null) {
                book.setStockQuantity(request.getStockQuantity());
            }
            if (request.getAvailable() != null) {
                book.setAvailable(request.getAvailable());
            }
            if (request.getOfficial() != null) {
                book.setOfficial(request.getOfficial());
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
        response.setFinalPrice(book.getFinalPrice());
        response.setStockQuantity(book.getStockQuantity());
        response.setAvailable(book.getAvailable());
        response.setOfficial(book.getOfficial());
        response.setDescription(book.getDescription());
        response.setCoverType(book.getCoverType());
        response.setUpdatedAt(book.getUpdatedAt());
        if (book.getAuthor() != null) {
            author.setId(book.getAuthor().getId());
            author.setName(book.getAuthor().getName());
            response.setAuthor(author);
        }
        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
            List<BookUpdateResponse.Category> categories = book.getCategories().stream().map(
                    item -> new BookUpdateResponse.Category(item.getId(), item.getName()))
                    .collect(Collectors.toList());
            response.setCategories(categories);
        }
        return response;
    }

    public void deleteBook(Book book) {
        // xóa tất cả các danh mục liên quan đến sách trước khi xóa sách
        book.getCategories().forEach(category -> category.getBooks().remove(book));
        // xóa sách
        this.bookRepository.delete(book);
    }
}
