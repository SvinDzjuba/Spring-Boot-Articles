package com.example.springbootarticles.services;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.*;
import com.example.springbootarticles.repositories.ArticleRepository;
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

        User user = userRepo.findById(article.getAuthor())
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
        response.setSlug(article.getSlug());
        response.setCreated_at(article.getCreated_at());
        response.setUpdated_at(article.getUpdated_at());
        response.setAuthor(author);
        response.setFavoriteCount(article.getFavoriteCount());
        response.setTagList(article.getTagList());
        return response;
    }

    public List<Article> getArticlesHandler(String tag, String author, String favorited, Integer limit, Integer offset) {
        List<Article> filteredArticles = new ArrayList<>();

        if (tag != null) {
            filteredArticles.addAll(articleRepo.findByTagListContaining(tag));
        }
        if (author != null) {
            User articleAuthor = userRepo.findByUsername(author);
            if (articleAuthor != null) {
                String authorId = articleAuthor.getId();
                filteredArticles.addAll(getArticlesByAuthorId(authorId));
            }
        }
        if (favorited != null) {
            User user = userRepo.findByUsername(favorited);
            if (user != null) {
                filteredArticles.addAll(getUserFavoritedArticles(user));
            }
        }

        // If no filters are applied, fill the list with all articles
        if (filteredArticles.isEmpty()) {
            filteredArticles.addAll(articleRepo.findAll());
        }

        // Remove duplicates (based on article IDs)
        Set<String> articleIds = new HashSet<>();
        filteredArticles.removeIf(article -> !articleIds.add(article.getId()));

        // Apply limit and offset
        filteredArticles = applyOffsetAndLimit(offset, limit, filteredArticles);

        // Sort by descending order of date
        filteredArticles.sort(Comparator.comparing(Article::getCreated_at).reversed());

        return filteredArticles;
    }

    public List<Article> getFeedArticlesHandler(Integer limit, Integer offset) {
        User currentUser = customService.getAuthenticatedUser();
        List<Article> articles = new ArrayList<>();
        if (currentUser != null) {
            String[] followedUsers = currentUser.getFollowing();
            for (String followedUser : followedUsers) {
                User user = userRepo.findById(followedUser).orElse(null);
                if (user != null) {
                    articles.addAll(getArticlesByAuthorId(user.getId()));
                }
            }
        }
        // Remove duplicates (based on article IDs)
        Set<String> articleIds = new HashSet<>();
        articles.removeIf(article -> !articleIds.add(article.getId()));

        // Apply limit and offset
        articles = applyOffsetAndLimit(offset, limit, articles);

        // Sort by descending order of date
        articles.sort(Comparator.comparing(Article::getCreated_at).reversed());

        return articles;
    }

    public void createArticleHandler(ArticleRequest article) {
        User currentUser = customService.getAuthenticatedUser();
        Article newArticle = new Article();
        newArticle.setTitle(article.getTitle());
        newArticle.setSlug(customService.slugify(article.getTitle()));
        newArticle.setContent(article.getContent());
        newArticle.setDemo(article.getDemo());
        newArticle.setCreated_at(new Date());
        newArticle.setUpdated_at(new Date());
        newArticle.setAuthor(currentUser.getId());
        newArticle.setFavoriteCount(0);
        newArticle.setTagList(checkTagListAndCapitalize(article.getTagList()));
        articleRepo.save(newArticle);
    }

    public void updateArticleHandler(String slug, ArticleRequest article) throws NotFoundException {
        Article articleToUpdate = articleRepo.findBySlug(slug);
        if (articleToUpdate != null) {
            User currentUser = customService.getAuthenticatedUser();
            if (Objects.equals(articleToUpdate.getAuthor(), currentUser.getId())) {
                articleToUpdate.setTitle(
                        article.getTitle() == null || article.getTitle().isEmpty() ? articleToUpdate.getTitle() : article.getTitle());
                articleToUpdate.setSlug(customService.slugify(
                        article.getTitle() == null || article.getTitle().isEmpty() ? articleToUpdate.getTitle() : article.getTitle()));
                articleToUpdate.setDemo(
                        article.getDemo() == null || article.getDemo().isEmpty() ? articleToUpdate.getDemo() : article.getDemo());
                articleToUpdate.setContent(
                        article.getContent() == null || article.getContent().isEmpty() ? articleToUpdate.getContent() : article.getContent());
                articleToUpdate.setUpdated_at(new Date());
                articleToUpdate.setTagList(checkTagListAndCapitalize(article.getTagList()));
                articleRepo.save(articleToUpdate);
            }
        } else {
            throw new NotFoundException("Article not found!");
        }
    }

    public void deleteArticleHandler(String slug) {
        User currentUser = customService.getAuthenticatedUser();
        Article articleToDelete = articleRepo.findBySlug(slug);
        if (articleToDelete != null) {
            if (Objects.equals(articleToDelete.getAuthor(), currentUser.getId())) {
                articleRepo.deleteById(articleToDelete.getId());
            } else {
                throw new SecurityException("You are not the author of this article!");
            }
        } else {
            throw new NotFoundException("Article not found!");
        }
    }

    public void likeArticle(String slug, boolean like) {
        Article articleToUpdate = articleRepo.findBySlug(slug);
        if (articleToUpdate != null) {
            User currentUser = customService.getAuthenticatedUser();
            List<User> checkArticleDuplicate = userRepo.findByFavoriteArticlesContaining(articleToUpdate.getId());
            String[] userFavoriteArticles = new String[0];
            if (like) {
                if (checkArticleDuplicate.isEmpty()) {
                    articleToUpdate.setFavoriteCount(articleToUpdate.getFavoriteCount() + 1);
                    userFavoriteArticles = customService.addOrRemoveStringFromArray(
                            currentUser.getFavoriteArticles(), articleToUpdate.getId(), "add");
                }
            } else {
                if (!checkArticleDuplicate.isEmpty()) {
                    articleToUpdate.setFavoriteCount(articleToUpdate.getFavoriteCount() - 1);
                    userFavoriteArticles = customService.addOrRemoveStringFromArray(
                            currentUser.getFavoriteArticles(), articleToUpdate.getId(), "remove");
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
        return articleRepo.findByAuthor(id);
    }

    public List<Article> applyOffsetAndLimit(Integer offset, Integer limit, List<Article> articles) {
        offset = offset == null ? 0 : offset; // default offset to 0 if not specified
        limit = limit == null ? 20 : limit; // default limit to 20 if not specified
        if (offset > 0) {
            int startIndex = Math.min(offset, articles.size());
            articles = articles.subList(startIndex, articles.size());
        }
        if (limit > 0) {
            int endIndex = Math.min(limit, articles.size());
            articles = articles.subList(0, endIndex);
        }
        return articles;
    }

    public List<Article> getUserFavoritedArticles(User user) {
        String[] userFavorited = user.getFavoriteArticles();
        List<String> favoritedArticlesList = Arrays.asList(userFavorited);
        List<Article> articles = articleRepo.findAll();
        articles.removeIf(article -> !favoritedArticlesList.contains(article.getId()));
        return articles;
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public String[] checkTagListAndCapitalize(String[] tagList) {
        if (tagList != null) {
            String[] newTagList = Arrays.stream(tagList)
                    .filter(tag -> !tag.isEmpty())
                    .map(ArticleService::capitalizeFirstLetter) // Capitalize the first letter
                    .toArray(String[]::new);

            if (newTagList.length > 0) {
                return newTagList;
            }
        }
        return null;
    }
}
