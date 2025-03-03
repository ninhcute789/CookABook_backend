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

import NandK.CookABook.dto.article.ArticleCreationRequest;
import NandK.CookABook.dto.article.ArticleUpdateRequest;
import NandK.CookABook.dto.pagination.ResultPagination;
import NandK.CookABook.entity.Article;
import NandK.CookABook.service.ArticleService;
import NandK.CookABook.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ApiMessage("Tạo bài viết thành công")
    public ResponseEntity<Article> createArticle(@Valid @RequestBody ArticleCreationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.articleService.createArticle(request));
    }

    @GetMapping
    @ApiMessage("Lấy danh sách bài viết thành công")
    public ResponseEntity<ResultPagination> getAllArticles(
            @Filter Specification<Article> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.articleService.getAllArticles(spec, pageable));
    }

    @GetMapping("/{articleId}")
    @ApiMessage("Lấy bài viết thành công")
    public ResponseEntity<Article> getArticleById(@Valid @PathVariable Long articleId) {
        Article article = this.articleService.getArticleById(articleId);
        if (article == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else
            return ResponseEntity.status(HttpStatus.OK).body(article);
    }

    @PutMapping
    @ApiMessage("Cập nhật bài viết thành công")
    public ResponseEntity<Article> updateArticleById(@Valid @RequestBody ArticleUpdateRequest request) {
        Article article = this.articleService.updateArticleById(request);
        if (article == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else
            return ResponseEntity.status(HttpStatus.OK).body(article);
    }

    @DeleteMapping("/{articleId}")
    @ApiMessage("Xóa bài viết thành công")
    public ResponseEntity<Void> deleteArticleById(@Valid @PathVariable Long articleId) {
        this.articleService.deleteArticleById(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
