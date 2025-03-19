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

import NandK.CookABook.dto.request.author.AuthorCreationRequest;
import NandK.CookABook.dto.request.author.AuthorUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.entity.Author;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.AuthorService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ApiMessage("Tạo tác giả thành công")
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody AuthorCreationRequest request)
            throws IdInvalidException {
        boolean isAuthorNameExist = this.authorService.isAuthorNameExist(request.getName());
        if (isAuthorNameExist) {
            throw new IdInvalidException(
                    "Tác giả với tên = " + request.getName() + " đã tồn tại, vui lòng sử dụng tên khác");
        }
        return ResponseEntity.ok(this.authorService.createAuthor(request));
    }

    @GetMapping
    @ApiMessage("Lấy danh sách tác giả thành công")
    public ResponseEntity<ResultPagination> getAllAuthors(
            @Filter Specification<Author> spec, Pageable pageable) {
        return ResponseEntity.ok(this.authorService.getAllAuthors(spec, pageable));
    }

    @GetMapping("/{authorId}")
    @ApiMessage("Lấy tác giả thành công")
    public ResponseEntity<Author> getAuthorById(@Valid @PathVariable Long authorId)
            throws IdInvalidException {
        Author author = this.authorService.getAuthorById(authorId);
        if (author == null) {
            throw new IdInvalidException("Tác giả với id = " + authorId + " không tồn tại");
        }
        return ResponseEntity.ok(author);
    }

    @PutMapping
    @ApiMessage("Cập nhật tác giả thành công")
    public ResponseEntity<Author> updateAuthor(@Valid @RequestBody AuthorUpdateRequest request)
            throws IdInvalidException {
        Author author = this.authorService.getAuthorById(request.getId());
        if (author == null) {
            throw new IdInvalidException("Tác giả với id = " + request.getId() + " không tồn tại");
        }
        boolean isAuthorNameExist = this.authorService.isAuthorNameExist(request.getName());
        if (isAuthorNameExist) {
            throw new IdInvalidException(
                    "Tác giả với tên = " + request.getName() + " đã tồn tại, vui lòng sử dụng tên khác");
        }
        author = this.authorService.updateAuthor(request);
        return ResponseEntity.ok(author);
    }

    @DeleteMapping("/{authorId}")
    @ApiMessage("Xóa tác giả thành công")
    public ResponseEntity<Void> deleteAuthorById(@Valid @PathVariable Long authorId)
            throws IdInvalidException {
        Author author = this.authorService.getAuthorById(authorId);
        if (author == null) {
            throw new IdInvalidException("Tác giả với id = " + authorId + " không tồn tại");
        }
        this.authorService.deleteAuthorById(authorId);
        return ResponseEntity.ok(null);
    }
}
