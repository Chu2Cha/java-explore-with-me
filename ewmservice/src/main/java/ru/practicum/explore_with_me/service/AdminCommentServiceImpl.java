package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.dto.comment.RequestCommentDto;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.CommentMapper;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.repository.CommentRepository;
import ru.practicum.explore_with_me.service.interfaces.AdminCommentService;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto updateCommentByAdmin(Long eventId, Long commentId, RequestCommentDto requestCommentDto,
                                           LocalDateTime updateCommentTime) {
        Comment comment = findComment(commentId);
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new ConflictException("Неправильно указан id события!");
        }
        comment.setText(requestCommentDto.getText());
        comment.setCommentDate(updateCommentTime);
        comment.setEdited(true);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteCommentByAdmin(Long eventId, Long commentId) {
        Comment comment = findComment(commentId);
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new NotFoundException("У события " + eventId + " нет комментария " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment id = " + commentId + " is not found."));
    }

}
