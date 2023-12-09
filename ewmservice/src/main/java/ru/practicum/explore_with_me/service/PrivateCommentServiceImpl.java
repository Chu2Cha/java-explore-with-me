package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.dto.comment.RequestCommentDto;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.CommentMapper;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.CommentRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.service.interfaces.PrivateCommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private final CommentMapper commentMapper;

    @Override
    public CommentDto postNewComment(Long userId, Long eventId, RequestCommentDto requestCommentDto, LocalDateTime commentDate) {
        User author = findAuthor(userId);
        Event event = findEvent(eventId);
        Comment comment = Comment.builder()
                .author(author)
                .event(event)
                .commentDate(commentDate)
                .edited(false)
                .text(requestCommentDto.getText())
                .build();
        checkCommentValidation(comment);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateCommentByAuthor(Long userId, Long eventId, Long commentId,
                                            RequestCommentDto requestCommentDto, LocalDateTime updateCommentTime) {
        Comment comment = findComment(commentId);
        if (comment.getAuthor().getId() != userId) {
            throw new ConflictException("Пользователь не может редактировать чужой комментарий!");
        }
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new ConflictException("Неправильно указан id события!");
        }
        if (comment.getEdited().equals(true) && updateCommentTime.isBefore(comment.getCommentDate().plusHours(1))) {
            throw new ConflictException("Пользователю нельзя редактировать комментрарий чаще, чем раз в час.");
        }
        comment.setText(requestCommentDto.getText());
        comment.setCommentDate(updateCommentTime);
        comment.setEdited(true);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteComment(Long userId, Long eventId, Long commentId) {
        findAuthor(userId);
        findEvent(eventId);
        Comment comment = findComment(commentId);
        if (comment.getAuthor().getId() != userId) {
            throw new ConflictException("Нельзя удалить чужой комментарий!");
        }
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new ConflictException("У заданного события нет такого комментария!");
        }
        commentRepository.deleteById(commentId);
    }

    private void checkCommentValidation(Comment comment) {
        if (!comment.getEvent().getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя оставлять комментарии неопубликованным событиям!");
        }
        if (comment.getCommentDate().isBefore(comment.getEvent().getPublishedOn())) {
            throw new ConflictException("нельзя публиковать комментарий до публикации события!");
        }
        List<Comment> comments = commentRepository.findAllByAuthorIdAndEventId(
                comment.getAuthor().getId(), comment.getEvent().getId());
        if (!comments.isEmpty())
            throw new ConflictException("Нельзя оставлять больше одного комментария одному событию!");
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment id = " + commentId + " is not found."));
    }

    private User findAuthor(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден."));
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id " + eventId + " не найдено."));
    }
}
