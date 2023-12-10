package ru.practicum.explore_with_me.controller.private_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.dto.comment.RequestCommentDto;
import ru.practicum.explore_with_me.service.interfaces.PrivateCommentService;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}/comments")
@Slf4j
@Validated
public class PrivateCommentController {
    private final PrivateCommentService privateCommentService;

    public PrivateCommentController(PrivateCommentService privateCommentService) {
        this.privateCommentService = privateCommentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postNewComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody RequestCommentDto requestCommentDto) {
        log.info("user: post new comment: userId = {}, eventId = {}, comment = {}", userId, eventId, requestCommentDto.getText());
        LocalDateTime commentDate = LocalDateTime.now();
        return privateCommentService.postNewComment(userId, eventId, requestCommentDto, commentDate);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateCommentByAuthor(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long commentId,
            @Valid @RequestBody RequestCommentDto requestCommentDto) {
        LocalDateTime updateCommentTime = LocalDateTime.now();
        log.info("user: update comment, userid = {}, event id = {}, commentId = {}, comment = {}, updated on {}",
                userId, eventId, commentId, requestCommentDto.getText(), updateCommentTime);
        return privateCommentService.updateCommentByAuthor(userId, eventId, commentId,
                requestCommentDto, updateCommentTime);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long commentId) {
        log.info("user: delete comment: userId = {}, eventId = {}, commentId = {}", userId, eventId, commentId);
        privateCommentService.deleteComment(userId, eventId, commentId);
    }
}
