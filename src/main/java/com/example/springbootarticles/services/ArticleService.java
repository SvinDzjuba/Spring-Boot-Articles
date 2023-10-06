package com.example.springbootarticles.services;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.*;
import com.example.springbootarticles.repositories.ArticleRepository;
import com.example.springbootarticles.repositories.CommentRepository;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CustomService customService;

    public ArticleResponse getArticleWithDetails(String id, String type) {
        Article article = articleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Article not found!"));

        User user = userRepo.findById(article.getAuthor_id())
                .orElseThrow(() -> new NotFoundException("Author of article not found!"));

        // Convert User to Author
        Author author = Author.builder()
                .name(user.getName())
                .username(user.getUsername())
                .bio(user.getBio())
                .build();

        return getArticleResponse(type, article, author);
    }

    private static ArticleResponse getArticleResponse(String type, Article article, Author author) {
        ArticleResponse response = new ArticleResponse();
        response.setTitle(article.getTitle());
        if (Objects.equals(type, "DEMO")) {
            response.setContent(null);
        } else {
            response.setContent(article.getContent());
        }
        response.setId(article.getId());
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
        if (articleData.isPresent()) {
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

    public void deleteArticleHandler(String id) {
        User currentUser = customService.getAuthenticatedUser();
        Optional<Article> articleData = articleRepo.findById(id);
        if (articleData.isPresent()) {
            Article articleToDelete = articleData.get();
            if (Objects.equals(articleToDelete.getAuthor_id(), currentUser.getId())) {
                articleRepo.deleteById(id);
            } else {
                throw new SecurityException("You are not the author of this article!");
            }
        } else {
            throw new NotFoundException("Article not found!");
        }
    }

    public void likeArticle(String id, boolean like) {
        Optional<Article> articleData = articleRepo.findById(id);
        if (articleData.isPresent()) {
            Article articleToUpdate = articleData.get();
            User currentUser = customService.getAuthenticatedUser();
            List<User> checkArticleDuplicate = userRepo.findByFavoriteArticlesContaining(id);
            String[] userFavoriteArticles = new String[0];
            if (like) {
                if (checkArticleDuplicate.isEmpty()) {
                    articleToUpdate.setFavoriteCount(articleToUpdate.getFavoriteCount() + 1);
                    userFavoriteArticles = customService.addOrRemoveStringFromArray(currentUser.getFavoriteArticles(), id, "add");
                }
            } else {
                if (!checkArticleDuplicate.isEmpty()) {
                    articleToUpdate.setFavoriteCount(articleToUpdate.getFavoriteCount() - 1);
                    userFavoriteArticles = customService.addOrRemoveStringFromArray(currentUser.getFavoriteArticles(), id, "remove");
                }
            }
            currentUser.setFavoriteArticles(userFavoriteArticles);
            userRepo.save(currentUser);
            articleRepo.save(articleToUpdate);
        } else {
            throw new NotFoundException("Article not found!");
        }
    }

    public String[] getTagsList() {
        List<Article> allArticles = articleRepo.findAll();
        List<String> tags = new ArrayList<>();
        for (Article article : allArticles) {
            tags.addAll(Arrays.asList(article.getTagList()));
        }
        Set<String> uniqueTags = new HashSet<>(tags);
        return uniqueTags.toArray(new String[0]);
    }

    public List<Article> getArticlesByAuthorId(String id) {
        List<Article> articles = articleRepo.findAll();
        List<Article> articlesByAuthor = new ArrayList<>();
        for (Article article : articles) {
            if (Objects.equals(article.getAuthor_id(), id)) {
                articlesByAuthor.add(article);
            }
        }
        return articlesByAuthor;
    }
}
