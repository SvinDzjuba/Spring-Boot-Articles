package com.example.springbootarticles.services;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.Article;
import com.example.springbootarticles.models.ArticleResponse;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.ArticleRepository;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
}
