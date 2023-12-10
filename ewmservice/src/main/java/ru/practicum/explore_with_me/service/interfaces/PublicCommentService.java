package ru.practicum.explore_with_me.service.interfaces;

import ru.practicum.explore_with_me.dto.comment.CommentDto;

import java.util.List;

public interface PublicCommentService {
    List<CommentDto> getAllEventComments(Long eventId, int from, int size);

    CommentDto getComment(Long eventId, Long commentId);
}
