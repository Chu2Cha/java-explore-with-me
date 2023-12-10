package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.dto.comment.RequestCommentDto;

import java.time.LocalDateTime;

public interface AdminCommentService {
    CommentDto updateCommentByAdmin(Long eventId, Long commentId, RequestCommentDto requestCommentDto,
                                    LocalDateTime updateCommentTime);

    void deleteCommentByAdmin(Long eventId, Long commentId);
}
