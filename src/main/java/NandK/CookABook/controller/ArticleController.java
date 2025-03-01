package NandK.CookABook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import NandK.CookABook.dto.article.ArticleCreationRequest;
import NandK.CookABook.entity.Article;
import NandK.CookABook.service.ArticleService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@Valid @RequestBody ArticleCreationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.articleService.createArticle(request));
    }
}
