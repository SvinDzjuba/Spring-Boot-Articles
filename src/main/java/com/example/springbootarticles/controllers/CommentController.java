package com.example.springbootarticles.controllers;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.Comment;
import com.example.springbootarticles.models.CommentResponse;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.CommentRepository;
import com.example.springbootarticles.repositories.UserRepository;
import com.example.springbootarticles.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class CommentController {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/comments")
    public List<CommentResponse> getComments() {
        List<Comment> comments = commentRepo.findAll();
        List<CommentResponse> commentsList = new ArrayList<>();
        for (Comment comment : comments) {
            commentsList.add(commentService.getCommentWithDetails(comment.getId()));
        }
        return commentsList;
    }

    @PostMapping("/comments")
    public String saveComment(@RequestBody Comment comment) {
        comment.setId(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByUsername(authentication.getName());
        comment.setUser_id(user.getId());
        commentRepo.save(comment);
        return "Comment was added successfully!";
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") String id, @RequestBody Comment comment) {
        try {
            commentService.updateCommentHandler(id, comment);
            return new ResponseEntity<>("Comment was updated successfully!", HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comments/{id}")
    public String deleteComment(@PathVariable String id){
        commentRepo.deleteById(id);
        return "Comment with id: {" + id + "} was deleted successfully!";
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentResponse> showComment(@PathVariable String id) {
        CommentResponse commentResponse = commentService.getCommentWithDetails(id);
        return ResponseEntity.ok(commentResponse);
    }
}
