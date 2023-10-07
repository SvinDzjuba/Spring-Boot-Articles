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
public class CommentService {
    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CustomService customService;

    public CommentResponse getCommentWithDetails(String id) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found!"));

        Article article = articleRepo.findById(comment.getArticle_id())
                .orElseThrow(() -> new NotFoundException("Article not found!"));

        User user = userRepo.findById(comment.getUser_id())
                .orElseThrow(() -> new NotFoundException("User not found!"));

        // Convert User to Author
        Author author = Author.builder()
                .name(user.getName())
                .username(user.getUsername())
                .bio(user.getBio())
                .build();

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setCreated_at(comment.getCreated_at());
        commentResponse.setUpdated_at(comment.getUpdated_at());
        commentResponse.setArticleTitle(article.getTitle());
        commentResponse.setAuthor(author);

        return commentResponse;
    }
    public void updateCommentHandler(String slug, String id, String content) throws NotFoundException {
        Optional<Comment> commentData = commentRepo.findById(id);
        if (commentData.isPresent()) {
            Comment commentToUpdate = checkCommentExistence(commentData.get(), slug);
            commentToUpdate.setContent(content == null ? commentToUpdate.getContent() : content);
            commentToUpdate.setUpdated_at(new Date());
            commentRepo.save(commentToUpdate);
        } else {
            throw new NotFoundException("Comment not found!");
        }
    }

    private Comment checkCommentExistence(Comment comment, String articleSlug) {
        User currentUser = customService.getAuthenticatedUser();
        Article article = articleRepo.findBySlug(articleSlug);
        if (article == null) {
            throw new NotFoundException("Article not found!");
        }
        if (!Objects.equals(comment.getUser_id(), currentUser.getId())) {
            throw new NotFoundException("You are not the author of this comment!");
        }
        if (!Objects.equals(comment.getArticle_id(), article.getId())) {
            throw new NotFoundException("Comment is not related to this article!");
        }
        return comment;
    }

    public void deleteCommentHandler(String slug, String id) {
        Optional<Comment> commentData = commentRepo.findById(id);
        if (commentData.isPresent()) {
            Comment comment = checkCommentExistence(commentData.get(), slug);
            commentRepo.deleteById(comment.getId());
        } else {
            throw new NotFoundException("Comment not found!");
        }
    }
    public List<CommentResponse> getArticleComments(String slug) {
        Article articleData = articleRepo.findBySlug(slug);
        if (articleData != null) {
            List<Comment> allComments = commentRepo.findAll();
            List<CommentResponse> comments = new ArrayList<>();
            for (Comment comment : allComments) {
                if (Objects.equals(comment.getArticle_id(), articleData.getId())) {
                    comments.add(this.getCommentWithDetails(comment.getId()));
                }
            }
            return comments;
        } else {
            throw new NotFoundException("Article not found!");
        }
    }
    public void saveCommentHandler(String content, String slug) {
        Article article = articleRepo.findBySlug(slug);
        if (article != null) {
            User currentUser = customService.getAuthenticatedUser();
            Comment comment = new Comment();
            comment.setUser_id(currentUser.getId());
            comment.setArticle_id(article.getId());
            comment.setContent(content);
            comment.setCreated_at(new Date());
            comment.setUpdated_at(new Date());
            commentRepo.save(comment);
        } else {
            throw new NotFoundException("Article not found!");
        }
    }

    public List<Comment> getCommentsByAuthorId(String id) {
        List<Comment> comments = commentRepo.findAll();
        List<Comment> commentsByAuthor = new ArrayList<>();
        for (Comment comment : comments) {
            if (Objects.equals(comment.getUser_id(), id)) {
                commentsByAuthor.add(comment);
            }
        }
        return commentsByAuthor;
    }
}
