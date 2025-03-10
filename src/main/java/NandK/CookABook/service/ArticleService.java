package NandK.CookABook.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import NandK.CookABook.dto.request.ArticleCreationRequest;
import NandK.CookABook.dto.request.ArticleUpdateRequest;
import NandK.CookABook.dto.response.ArticleCreationResponse;
import NandK.CookABook.dto.response.ArticleFoundResponse;
import NandK.CookABook.dto.response.ArticleUpdateResponse;
import NandK.CookABook.dto.response.ResultPagination;
import NandK.CookABook.entity.Article;
import NandK.CookABook.entity.User;
import NandK.CookABook.repository.ArticleRepository;
import NandK.CookABook.repository.UserRepository;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public Article createArticle(ArticleCreationRequest request) {
        Article article = new Article();
        // check user
        if (request.getUser() != null && request.getUser().getId() != null) {
            Optional<User> userOptional = this.userRepository.findById(request.getUser().getId());
            article.setUser(userOptional.isPresent() ? userOptional.get() : null);
        } else {
            article.setUser(null);
        }

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setImageURL(request.getImageURL());

        return this.articleRepository.save(article);
    }

    public ArticleCreationResponse convertToArticleCreationResponse(Article article) {
        ArticleCreationResponse response = new ArticleCreationResponse();
        ArticleCreationResponse.User user = new ArticleCreationResponse.User();

        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setImageURL(article.getImageURL());
        response.setCreatedAt(article.getCreatedAt());
        if (article.getUser() != null) {
            user.setId(article.getUser().getId());
            user.setName(article.getUser().getName());
            response.setUser(user);
        }
        return response;
    }

    public ResultPagination getAllArticles(Specification<Article> spec, Pageable pageable) {
        Page<Article> articles = this.articleRepository.findAll(spec, pageable);
        ResultPagination result = new ResultPagination();
        ResultPagination.Meta meta = new ResultPagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setTotalPage(articles.getTotalPages());
        meta.setTotalElement(articles.getTotalElements());

        result.setMeta(meta);

        List<ArticleFoundResponse> listArticle = articles.getContent().stream().map(
                item -> new ArticleFoundResponse(
                        item.getId(),
                        item.getTitle(),
                        item.getContent(),
                        item.getImageURL(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getUser() != null ? new ArticleFoundResponse.User(
                                item.getUser().getId(),
                                item.getUser().getName()) : null))
                .collect(Collectors.toList());
        result.setData(listArticle);
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

    public ArticleFoundResponse convertToArticleFoundResponse(Article article) {
        ArticleFoundResponse response = new ArticleFoundResponse();
        ArticleFoundResponse.User user = new ArticleFoundResponse.User();

        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setImageURL(article.getImageURL());
        response.setCreatedAt(article.getCreatedAt());
        response.setUpdatedAt(article.getUpdatedAt());
        if (article.getUser() != null) {
            user.setId(article.getUser().getId());
            user.setName(article.getUser().getName());
            response.setUser(user);
        }
        return response;
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
            if (request.getUser() != null && request.getUser().getId() != null) {
                Optional<User> userOptional = this.userRepository.findById(request.getUser().getId());
                article.setUser(userOptional.isPresent() ? userOptional.get() : null);
            } else {
                article.setUser(null);
            }
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
        if (article.getUser() != null) {
            user.setId(article.getUser().getId());
            user.setName(article.getUser().getName());
            response.setUser(user);
        }
        return response;
    }

    public void deleteArticleById(Long articleId) {
        this.articleRepository.deleteById(articleId);
    }
}
