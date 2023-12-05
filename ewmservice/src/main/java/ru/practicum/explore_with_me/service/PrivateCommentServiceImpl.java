package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.dto.comment.NewCommentDto;
import ru.practicum.explore_with_me.dto.enums.EventState;
import ru.practicum.explore_with_me.exceptions.ConflictException;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.CommentMapper;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.model.Comment;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.CommentRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.service.interfaces.PrivateCommentService;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto postNewComment(Long userId, Long eventId, NewCommentDto newCommentDto, LocalDateTime commentDate) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id " + eventId + " не найдено."));
        Comment comment = Comment.builder()
                .author(author)
                .event(event)
                .commentDate(commentDate)
                .edited(false)
                .text(newCommentDto.getText())
                .build();
        checkCommentValidation(comment);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }
    private void checkCommentValidation(Comment comment){
        if(!comment.getEvent().getState().equals(EventState.PUBLISHED)){
            throw new ConflictException("Нельзя оставлять комментарии неопубликованным событиям!");
        }
        if(comment.getCommentDate().isBefore(comment.getEvent().getPublishedOn())){
            throw new ConflictException("нельзя публиковать комментарий до публикации события!");
        }
        List<Comment> comments = commentRepository.findAllByAuthorIdAndEventId(
                comment.getAuthor().getId(), comment.getEvent().getId());
        if(comments.size()>=1)
            throw new ConflictException("Нельзя оставлять больше одного комментария одному событию!");
    }
}
