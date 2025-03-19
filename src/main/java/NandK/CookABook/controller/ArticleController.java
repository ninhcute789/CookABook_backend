package NandK.CookABook.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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

import NandK.CookABook.dto.request.article.ArticleCreationRequest;
import NandK.CookABook.dto.request.article.ArticleUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.article.ArticleCreationResponse;
import NandK.CookABook.dto.response.article.ArticleFoundResponse;
import NandK.CookABook.dto.response.article.ArticleUpdateResponse;
import NandK.CookABook.entity.Article;
import NandK.CookABook.exception.IdInvalidException;
import NandK.CookABook.service.ArticleService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ApiMessage("Tạo bài viết thành công")
    public ResponseEntity<ArticleCreationResponse> createArticle(
            @Valid @RequestBody ArticleCreationRequest request) {
        Article article = this.articleService.createArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.articleService.convertToArticleCreationResponse(article));
    }

    @GetMapping("/all")
    @ApiMessage("Lấy danh sách bài viết thành công")
    public ResponseEntity<ResultPagination> getAllArticles(
            @Filter Specification<Article> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.articleService.getAllArticles(spec, pageable));
    }

    @GetMapping("/{articleId}")
    @ApiMessage("Lấy bài viết thành công")
    public ResponseEntity<ArticleFoundResponse> getArticleById(@Valid @PathVariable Long articleId)
            throws IdInvalidException {
        Article article = this.articleService.getArticleById(articleId);
        if (article == null) {
            throw new IdInvalidException("Bài viết với id = " + articleId + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.articleService.convertToArticleFoundResponse(article));
    }

    @PutMapping
    @ApiMessage("Cập nhật bài viết thành công")
    public ResponseEntity<ArticleUpdateResponse> updateArticleById(
            @Valid @RequestBody ArticleUpdateRequest request)
            throws IdInvalidException {
        Article article = this.articleService.updateArticle(request);
        if (article == null) {
            throw new IdInvalidException("Bài viết với id = " + request.getId() + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.articleService.convertToArticleUpdateResponse(article));
    }

    @DeleteMapping("/{articleId}")
    @ApiMessage("Xóa bài viết thành công")
    public ResponseEntity<Void> deleteArticleById(@Valid @PathVariable Long articleId)
            throws IdInvalidException {
        Article article = this.articleService.getArticleById(articleId);
        if (article == null) {
            throw new IdInvalidException("Bài viết với id = " + articleId + " không tồn tại");
        }
        this.articleService.deleteArticleById(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
