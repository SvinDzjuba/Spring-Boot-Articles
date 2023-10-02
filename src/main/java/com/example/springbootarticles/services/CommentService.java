package com.example.springbootarticles.services;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.Article;
import com.example.springbootarticles.models.Comment;
import com.example.springbootarticles.models.CommentResponse;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.ArticleRepository;
import com.example.springbootarticles.repositories.CommentRepository;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private UserRepository userRepo;

    public CommentResponse getCommentWithDetails(String id) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found!"));

        Article article = articleRepo.findById(comment.getArticle_id())
                .orElseThrow(() -> new NotFoundException("Article not found!"));

        User author = userRepo.findById(comment.getUser_id())
                .orElseThrow(() -> new NotFoundException("User not found!"));

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setCreated_at(comment.getCreated_at());
        commentResponse.setUpdated_at(comment.getUpdated_at());
        commentResponse.setArticle(article);
        commentResponse.setAuthor(author);

        return commentResponse;
    }

    public void updateCommentHandler(String id, Comment comment) throws NotFoundException {
        Optional<Comment> commentData = commentRepo.findById(id);
        if (commentData.isPresent()){
            Comment commentToUpdate = commentData.get();
            User user = userRepo.findById(commentToUpdate.getUser_id())
                    .orElseThrow(() -> new NotFoundException("User of comment not found!"));
            Article article = articleRepo.findById(commentToUpdate.getArticle_id())
                    .orElseThrow(() -> new NotFoundException("Article of comment not found!"));
            commentToUpdate.setContent(comment.getContent() == null ? commentToUpdate.getContent() : comment.getContent());
            commentToUpdate.setUpdated_at(new Date());
            commentToUpdate.setUser_id(user.getId() == null ? commentToUpdate.getUser_id() : user.getId());
            commentToUpdate.setArticle_id(article.getId() == null ? commentToUpdate.getArticle_id() : article.getId());
            commentRepo.save(commentToUpdate);
        } else {
            throw new NotFoundException("Comment not found!");
        }
    }
}
