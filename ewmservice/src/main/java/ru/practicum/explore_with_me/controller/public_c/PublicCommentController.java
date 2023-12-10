package ru.practicum.explore_with_me.controller.public_c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.service.interfaces.PublicCommentService;

import java.util.List;

@RestController
@RequestMapping(path = "/events/{eventId}/comments")
@Slf4j
public class PublicCommentController {
    private final PublicCommentService publicCommentService;

    public PublicCommentController(PublicCommentService publicCommentService) {
        this.publicCommentService = publicCommentService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllEventComments(
            @PathVariable Long eventId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("get all comments for  eventid = {}", eventId);
        return publicCommentService.getAllEventComments(eventId, from, size);
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getComment(
            @PathVariable Long eventId,
            @PathVariable Long commentId) {
        log.info("get one comment where commentId = {} for  eventid = {}", commentId, eventId);
        return publicCommentService.getComment(eventId, commentId);
    }
}
