package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.dto.comment.RequestCommentDto;

import java.time.LocalDateTime;

public interface PrivateCommentService {
    CommentDto postNewComment(Long userId, Long eventId, RequestCommentDto requestCommentDto, LocalDateTime commentDate);

    CommentDto updateCommentByAuthor(Long userId, Long eventId, Long commentId,
                                     RequestCommentDto requestCommentDto, LocalDateTime updateCommentTime);

    void deleteComment(Long userId, Long eventId, Long commentId);
}
