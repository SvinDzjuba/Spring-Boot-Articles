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

        Article article = articleRepo.findById(comment.getArticle_id().toString())
                .orElseThrow(() -> new NotFoundException("Article not found!"));

        User author = userRepo.findById(comment.getUser_id().toString())
                .orElseThrow(() -> new NotFoundException("User not found!"));

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setContent(comment.getContent());
        commentResponse.setCreated_at(comment.getCreated_at());
        commentResponse.setUpdated_at(comment.getUpdated_at());
        commentResponse.setArticle(article);
        commentResponse.setAuthor(author);

        return commentResponse;
    }
}
