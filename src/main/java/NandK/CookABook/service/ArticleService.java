package NandK.CookABook.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.article.ArticleCreationRequest;
import NandK.CookABook.dto.article.ArticleUpdateRequest;
import NandK.CookABook.dto.pagination.Meta;
import NandK.CookABook.dto.pagination.ResultPagination;
import NandK.CookABook.entity.Article;
import NandK.CookABook.repository.ArticleRepository;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article createArticle(ArticleCreationRequest request) {
        Article article = new Article();

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setImageURL(request.getImageURL());

        return this.articleRepository.save(article);
    }

    public ResultPagination getAllArticles(Specification<Article> spec, Pageable pageable) {
        Page<Article> articles = this.articleRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setTotalPage(articles.getTotalPages());
        meta.setTotalElement(articles.getTotalElements());

        result.setMeta(meta);
        result.setData(articles.getContent());
        return result;
    }

    public Article getArticleById(Long articleId) {
        Optional<Article> article = this.articleRepository.findById(articleId);
        if (article.isPresent()) {
            return article.get();
        } else {
            return null;
        }
    }

    public Article updateArticleById(ArticleUpdateRequest request) {
        Article article = this.getArticleById(request.getId());
        if (article != null) {
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                article.setTitle(request.getTitle());
            }
            if (request.getContent() != null && !request.getContent().isBlank()) {
                article.setContent(request.getContent());
            }
            if (request.getImageURL() != null && !request.getImageURL().isBlank()) {
                article.setImageURL(request.getImageURL());
            }
            return this.articleRepository.save(article);
        } else {
            return null;
        }
    }

    public void deleteArticleById(Long articleId) {
        this.articleRepository.deleteById(articleId);
    }
}
