package com.example.springbootarticles.services;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.Article;
import com.example.springbootarticles.models.ArticleResponse;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.ArticleRepository;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private UserRepository userRepo;

    public ArticleResponse getArticleWithDetails(String id, String type) {
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Article not found!"));

        User author = userRepo.findById(article.getAuthor_id())
                .orElseThrow(() -> new NotFoundException("Author of article not found!"));

        ArticleResponse response = new ArticleResponse();
        response.setTitle(article.getTitle());
        if (Objects.equals(type, "DEMO")) {
            response.setContent(null);
        } else {
            response.setContent(article.getContent());
        }
        response.setDemo(article.getDemo());
        response.setCreated_at(article.getCreated_at());
        response.setUpdated_at(article.getUpdated_at());
        response.setAuthor(author);
        response.setFavoriteCount(article.getFavoriteCount());
        response.setTagList(article.getTagList());

        return response;
    }
    public void updateArticleHandler(String id, Article article) throws NotFoundException {
        Optional<Article> articleData = articleRepo.findById(id);
        if (articleData.isPresent()){
            Article articleToUpdate = articleData.get();
            User author = userRepo.findById(articleToUpdate.getAuthor_id())
                    .orElseThrow(() -> new NotFoundException("Author of article not found!"));
            articleToUpdate.setTitle(article.getTitle() == null ? articleToUpdate.getTitle() : article.getTitle());
            articleToUpdate.setDemo(article.getDemo() == null ? articleToUpdate.getDemo() : article.getDemo());
            articleToUpdate.setContent(article.getContent() == null ? articleToUpdate.getContent() : article.getContent());
            articleToUpdate.setAuthor_id(author.getId() == null ? articleToUpdate.getAuthor_id() : author.getId());
            articleToUpdate.setUpdated_at(new Date());
            articleToUpdate.setFavoriteCount(article.getFavoriteCount() == 0 ? articleToUpdate.getFavoriteCount() : article.getFavoriteCount());
            articleToUpdate.setTagList(article.getTagList().length == 0 ? articleToUpdate.getTagList() : article.getTagList());
            articleRepo.save(articleToUpdate);
        } else {
            throw new NotFoundException("Article not found!");
        }
    }
}
