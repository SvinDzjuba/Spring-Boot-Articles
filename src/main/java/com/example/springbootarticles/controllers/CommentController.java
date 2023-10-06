package com.example.springbootarticles.controllers;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.Comment;
import com.example.springbootarticles.models.CommentResponse;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.CommentRepository;
import com.example.springbootarticles.services.CommentService;
import com.example.springbootarticles.services.CustomService;
import com.example.springbootarticles.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "api")
@Tag(name = "Comments", description = "Comments API")
public class CommentController {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CustomService customService;

    @GetMapping("/articles/{id}/comments")
    @Operation(summary = "Get Comments from an Article")
    public List<CommentResponse> getComments(@PathVariable String id) {
        return commentService.getArticleComments(id);
    }

    @PostMapping("/articles/{id}/comments")
    @Operation(summary = "Add Comments to an Article")
    public ResponseEntity<?> saveComment(@RequestBody String content, @PathVariable String id) {
        try {
            commentService.saveCommentHandler(content, id);
            return new ResponseEntity<>("Comment was added successfully!", HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("Article now found!", HttpStatus.NOT_FOUND);
        }
    }

//    @PutMapping("/articles/{id}/comments/{id}")
//    @Operation(summary = "Update comment by id")
//    public ResponseEntity<?> updateComment(@PathVariable("id") String id, @RequestBody String content) {
//        try {
//            commentService.updateCommentHandler(id, content);
//            return new ResponseEntity<>("Comment was updated successfully!", HttpStatus.OK);
//        } catch (ConstraintViolationException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
//        } catch (NotFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }

//    @DeleteMapping("/articles/{id}/comments/{id}")
//    @Operation(summary = "Delete Comment")
//    public ResponseEntity<?> deleteComment(@PathVariable String id){
//
//    }
}
