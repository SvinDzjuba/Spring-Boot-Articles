package com.example.springbootarticles.controllers;

import com.example.springbootarticles.models.Comment;
import com.example.springbootarticles.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepo;

    @GetMapping("/comments")
    public List<Comment> getComments(){
        return commentRepo.findAll();
    }

    @PostMapping("/comments")
    public String saveComment(@RequestBody Comment comment){
        commentRepo.save(comment);
        return "Comment was added successfully";
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") String id, @RequestBody Comment comment)
    {
        Optional<Comment> commentData = commentRepo.findById(id);

        if (commentData.isPresent()){
            Comment _comment = commentData.get();
            _comment.setArticle_id(comment.getArticle_id());
            _comment.setUser_id(comment.getUser_id());
            _comment.setContent(comment.getContent());
            _comment.setCreated_at(comment.getCreated_at());
            _comment.setUpdated_at(comment.getUpdated_at());

            return new ResponseEntity<>(commentRepo.save(_comment), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comments/{id}")
    public String deleteComment(@PathVariable String id){
        commentRepo.deleteById(id);
        return "Comment with id: {" + id + "} was deleted successfully";
    }
}
