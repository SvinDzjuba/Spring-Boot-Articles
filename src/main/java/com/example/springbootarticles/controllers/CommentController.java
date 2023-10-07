package com.example.springbootarticles.controllers;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.CommentResponse;
import com.example.springbootarticles.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping(path = "api")
@Tag(name = "Comments", description = "Comments API")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/articles/{slug}/comments")
    @Operation(summary = "Get Comments from an Article")
    public List<CommentResponse> getComments(
            @PathVariable @Parameter(name = "slug", example = "why-i-believe-scratch-is-the-future-of-programming") String slug)
    {
        return commentService.getArticleComments(slug);
    }

    @PostMapping("/articles/{slug}/comments")
    @Operation(summary = "Add Comments to an Article")
    public ResponseEntity<?> saveComment(
            @RequestBody @Schema(name = "content", example = "I definitely agree with this article!") String content,
            @PathVariable @Parameter(name = "slug", example = "why-i-believe-scratch-is-the-future-of-programming") String slug)
    {
        try {
            commentService.saveCommentHandler(content, slug);
            return new ResponseEntity<>("Comment was added successfully!", HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("Article now found!", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/articles/{slug}/comments/{id}")
    @Operation(summary = "Update Comment")
    public ResponseEntity<?> updateComment(
            @PathVariable @Parameter(name = "slug", example = "why-i-believe-scratch-is-the-future-of-programming") String slug,
            @PathVariable @Parameter(name = "id", example = "65217b12e7aa345f6bc16a43") String id,
            @RequestBody @Schema(name = "content", example = "I definitely agree with this article!") String content)
    {
        try {
            commentService.updateCommentHandler(slug, id, content);
            return new ResponseEntity<>("Comment was updated successfully!", HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/articles/{slug}/comments/{id}")
    @Operation(summary = "Delete Comment")
    public ResponseEntity<?> deleteComment(
            @PathVariable @Parameter(name = "slug", example = "why-i-believe-scratch-is-the-future-of-programming") String slug,
            @PathVariable @Parameter(name = "id", example = "65217b12e7aa345f6bc16a43") String id)
    {
        try {
            commentService.deleteCommentHandler(slug, id);
            return new ResponseEntity<>("Comment was deleted successfully!", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
