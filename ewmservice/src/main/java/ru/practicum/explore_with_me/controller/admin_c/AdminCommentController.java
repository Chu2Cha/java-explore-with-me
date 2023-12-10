package ru.practicum.explore_with_me.controller.admin_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.dto.comment.RequestCommentDto;
import ru.practicum.explore_with_me.service.interfaces.AdminCommentService;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/admin/events/{eventId}/comments")
@Slf4j
@Validated
public class AdminCommentController {
    private final AdminCommentService adminCommentService;

    public AdminCommentController(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateCommentByAuthor(
            @PathVariable Long eventId,
            @PathVariable Long commentId,
            @Valid @RequestBody RequestCommentDto requestCommentDto) {
        LocalDateTime updateCommentTime = LocalDateTime.now();
        log.info("admin: update comment, event id = {}, commentId = {}, comment = {}, updated on {}",
                eventId, commentId, requestCommentDto.getText(), updateCommentTime);
        return adminCommentService.updateCommentByAdmin(eventId, commentId,
                requestCommentDto, updateCommentTime);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long eventId,
            @PathVariable Long commentId) {
        log.info("admin: delete comment: eventId = {}, commentId = {}", eventId, commentId);
        adminCommentService.deleteCommentByAdmin(eventId, commentId);
    }
}
