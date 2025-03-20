package NandK.CookABook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.author.AuthorCreationRequest;
import NandK.CookABook.dto.request.author.AuthorUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.author.AuthorCreationResponse;
import NandK.CookABook.dto.response.author.AuthorFoundResponse;
import NandK.CookABook.dto.response.author.AuthorUpdateResponse;
import NandK.CookABook.entity.Author;
import NandK.CookABook.entity.Book;
import NandK.CookABook.repository.AuthorRepository;
import NandK.CookABook.repository.BookRepository;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public boolean isAuthorNameExist(String name) {
        return this.authorRepository.existsByName(name);
    }

    public Author createAuthor(AuthorCreationRequest request) {
        Author author = new Author();

        author.setName(request.getName());

        return this.authorRepository.save(author);
    }

    public AuthorCreationResponse convertToAuthorCreationResponse(Author author) {
        AuthorCreationResponse authorCreationResponse = new AuthorCreationResponse();
        authorCreationResponse.setId(author.getId());
        authorCreationResponse.setName(author.getName());
        authorCreationResponse.setCreatedAt(author.getCreatedAt());
        return authorCreationResponse;
    }

    public Integer getNumberOfBooksByAuthorId(Long authorId) {
        Author author = this.getAuthorById(authorId);
        Integer numberOfBooks = bookRepository.countByAuthorId(authorId);
        author.setNumberOfBooks(numberOfBooks);
        return numberOfBooks;
    }

    public ResultPagination getAllAuthors(Specification<Author> spec, Pageable pageable) {
        Page<Author> authors = this.authorRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(authors.getTotalPages());
        meta.setTotalElements(authors.getTotalElements());

        result.setMeta(meta);

        List<AuthorFoundResponse> listAuthors = new ArrayList<>();
        for (Author author : authors.getContent()) {
            AuthorFoundResponse authorFoundResponse = new AuthorFoundResponse();
            authorFoundResponse.setId(author.getId());
            authorFoundResponse.setName(author.getName());
            authorFoundResponse.setNumberOfBooks(this.getNumberOfBooksByAuthorId(author.getId()));
            authorFoundResponse.setBookIds(this.getBookIdsByAuthor(author));
            authorFoundResponse.setCreatedAt(author.getCreatedAt());
            authorFoundResponse.setUpdatedAt(author.getUpdatedAt());
            listAuthors.add(authorFoundResponse);
        }
        result.setData(listAuthors);
        return result;
    }

    public Author getAuthorById(Long authorId) {
        Optional<Author> author = this.authorRepository.findById(authorId);
        if (author.isPresent()) {
            return author.get();
        } else {
            return null;
        }
    }

    public List<String> getBookIdsByAuthor(Author author) {
        List<String> bookIds = new ArrayList<>();
        for (Book book : author.getBooks()) {
            bookIds.add(book.getId().toString());
        }
        return bookIds;
    }

    public AuthorFoundResponse convertToAuthorFindByIdResponse(Author author) {
        AuthorFoundResponse authorFoundResponse = new AuthorFoundResponse();
        authorFoundResponse.setId(author.getId());
        authorFoundResponse.setName(author.getName());
        authorFoundResponse.setNumberOfBooks(this.getNumberOfBooksByAuthorId(author.getId()));
        authorFoundResponse.setBookIds(this.getBookIdsByAuthor(author));
        authorFoundResponse.setCreatedAt(author.getCreatedAt());
        authorFoundResponse.setUpdatedAt(author.getUpdatedAt());
        return authorFoundResponse;
    }

    public Author updateAuthor(AuthorUpdateRequest request) {
        Author author = this.getAuthorById(request.getId());
        if (request.getName() != null && !request.getName().isBlank()) {
            author.setName(request.getName());
        }
        return this.authorRepository.save(author);
    }

    public AuthorUpdateResponse convertToAuthorUpdateResponse(Author author) {
        AuthorUpdateResponse authorUpdateResponse = new AuthorUpdateResponse();
        authorUpdateResponse.setId(author.getId());
        authorUpdateResponse.setName(author.getName());
        authorUpdateResponse.setUpdatedAt(author.getUpdatedAt());
        return authorUpdateResponse;
    }

    public void deleteAuthorById(Long authorId) {
        Author author = this.getAuthorById(authorId);
        for (Book book : author.getBooks()) {
            book.setAuthor(null);
            this.bookRepository.save(book);
        }
        this.authorRepository.delete(author);
    }
}
