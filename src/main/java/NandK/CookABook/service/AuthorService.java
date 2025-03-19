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
import NandK.CookABook.dto.response.author.AuthorFoundResponse;
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
        author.setNumberOfBooks(0);

        return this.authorRepository.save(author);
    }

    public Integer getNumberOfBooksByAuthorId(Long authorId) {
        return bookRepository.countByAuthorId(authorId);
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
            author.get().setNumberOfBooks(this.getNumberOfBooksByAuthorId(authorId));
            return author.get();
        } else {
            return null;
        }
    }

    public Author updateAuthor(AuthorUpdateRequest request) {
        Author author = this.getAuthorById(request.getId());
        if (request.getName() != null && !request.getName().isBlank()) {
            author.setName(request.getName());
        }
        return this.authorRepository.save(author);
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
