package ru.practicum.explore_with_me.controller.private_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.dto.comment.NewCommentDto;
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
            @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("post new comment: userId = {}, eventId = {}, comment = {}", userId, eventId, newCommentDto.getText());
        LocalDateTime commentDate = LocalDateTime.now();
        return privateCommentService.postNewComment(userId, eventId, newCommentDto, commentDate);

    }
}
