package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.article.ArticleCreationRequest;
import NandK.CookABook.dto.request.article.ArticleUpdateRequest;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.dto.response.article.ArticleCreationResponse;
import NandK.CookABook.dto.response.article.ArticleFoundResponse;
import NandK.CookABook.dto.response.article.ArticleUpdateResponse;
import NandK.CookABook.entity.Article;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.ArticleRepository;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    public Article createArticle(ArticleCreationRequest request) {
        User user = this.userService.getUserById(request.getUserId());
        if (user != null) {
            Article article = new Article();
            article.setTitle(request.getTitle());
            article.setContent(request.getContent());
            article.setImageURL(request.getImageURL());
            article.setUser(user);
            return this.articleRepository.save(article);
        } else {
            return null;
        }
    }

    public ArticleCreationResponse convertToArticleCreationResponse(Article article) {
        ArticleCreationResponse response = new ArticleCreationResponse();
        ArticleCreationResponse.User user = new ArticleCreationResponse.User();

        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setImageURL(article.getImageURL());
        response.setCreatedAt(article.getCreatedAt());

        user.setId(article.getUser().getId());
        user.setName(article.getUser().getName());
        response.setUser(user);

        return response;
    }

    public ResultPagination getAllArticles(Specification<Article> spec, Pageable pageable) {
        Page<Article> articles = this.articleRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(articles.getTotalPages());
        meta.setTotalElements(articles.getTotalElements());

        result.setMeta(meta);

        List<ArticleFoundResponse> listArticles = this.convertToArticlesFoundResponse(articles.getContent());
        result.setData(listArticles);
        return result;
    }

    public ResultPagination getAllArticlesByUser(User user, Pageable pageable) {
        Page<Article> articles = this.articleRepository.findByUser(user, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotalPages(articles.getTotalPages());
        meta.setTotalElements(articles.getTotalElements());
        result.setMeta(meta);

        List<ArticleFoundResponse> listArticles = this.convertToArticlesFoundResponse(articles.getContent());
        result.setData(listArticles);
        return result;
    }

    public List<ArticleFoundResponse> convertToArticlesFoundResponse(List<Article> articles) {
        return articles.stream().map(
                item -> new ArticleFoundResponse(
                        item.getId(),
                        item.getTitle(),
                        item.getContent(),
                        item.getImageURL(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        new ArticleFoundResponse.User(
                                item.getUser().getId(),
                                item.getUser().getName())))
                .collect(Collectors.toList());
    }

    public Article getArticleById(Long articleId) {
        Optional<Article> article = this.articleRepository.findById(articleId);
        if (article.isPresent()) {
            return article.get();
        } else {
            return null;
        }
    }

    public ArticleFoundResponse convertToArticleFoundResponse(Article article) {
        ArticleFoundResponse response = new ArticleFoundResponse();
        ArticleFoundResponse.User user = new ArticleFoundResponse.User();

        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setImageURL(article.getImageURL());
        response.setCreatedAt(article.getCreatedAt());
        response.setUpdatedAt(article.getUpdatedAt());

        user.setId(article.getUser().getId());
        user.setName(article.getUser().getName());
        response.setUser(user);

        return response;
    }

    public Article updateArticle(ArticleUpdateRequest request) {
        Article article = this.getArticleById(request.getId());
        User user = this.userService.getUserById(request.getUserId());
        if (article != null && user != null) {
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                article.setTitle(request.getTitle());
            }
            if (request.getContent() != null && !request.getContent().isBlank()) {
                article.setContent(request.getContent());
            }
            if (request.getImageURL() != null && !request.getImageURL().isBlank()) {
                article.setImageURL(request.getImageURL());
            }
            article.setUser(user);
            return this.articleRepository.save(article);
        } else {
            return null;
        }
    }

    public ArticleUpdateResponse convertToArticleUpdateResponse(Article article) {
        ArticleUpdateResponse response = new ArticleUpdateResponse();
        ArticleUpdateResponse.User user = new ArticleUpdateResponse.User();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setImageURL(article.getImageURL());
        response.setUpdatedAt(article.getUpdatedAt());

        user.setId(article.getUser().getId());
        user.setName(article.getUser().getName());
        response.setUser(user);

        return response;
    }

    public void deleteArticle(Article article) {
        this.articleRepository.delete(article);
    }
}
