package NandK.CookABook.service;

import org.springframework.stereotype.Service;

import NandK.CookABook.dto.article.ArticleCreationRequest;
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
        article.setCreatedAt(request.getCreatedAt());
        article.setUpdatedAt(request.getUpdatedAt());
        article.setCreateBy(request.getCreateBy());
        article.setUpdateBy(request.getUpdateBy());

        return this.articleRepository.save(article);
    }
}
