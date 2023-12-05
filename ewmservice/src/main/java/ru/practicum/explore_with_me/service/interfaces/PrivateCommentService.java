package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.dto.comment.NewCommentDto;

import java.time.LocalDateTime;

public interface PrivateCommentService {
    CommentDto postNewComment(Long userId, Long eventId, NewCommentDto newCommentDto, LocalDateTime commentDate);
}
