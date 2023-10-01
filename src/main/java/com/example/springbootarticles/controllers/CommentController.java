package com.example.springbootarticles.controllers;

import com.example.springbootarticles.models.Comment;
import com.example.springbootarticles.models.CommentResponse;
import com.example.springbootarticles.repositories.CommentRepository;
import com.example.springbootarticles.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class CommentController {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    public List<Comment> getComments() {
        return commentRepo.findAll();
    }

    @PostMapping("/comments")
    public String saveComment(@RequestBody Comment comment) {
        commentRepo.save(comment);
        return "Comment was added successfully";
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") String id, @RequestBody Comment comment) {
        Optional<Comment> commentData = commentRepo.findById(id);
        if (commentData.isPresent()){
            Comment newComment = new Comment(
                    id,
                    comment.getUser_id(),
                    comment.getArticle_id(),
                    comment.getContent(),
                    comment.getCreated_at(),
                    comment.getUpdated_at()
            );
            return new ResponseEntity<>(commentRepo.save(newComment), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comments/{id}")
    public String deleteComment(@PathVariable String id){
        commentRepo.deleteById(id);
        return "Comment with id: {" + id + "} was deleted successfully";
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentResponse> showComment(@PathVariable String id) {
        CommentResponse commentResponse = commentService.getCommentWithDetails(id);
        return ResponseEntity.ok(commentResponse);
    }
}
