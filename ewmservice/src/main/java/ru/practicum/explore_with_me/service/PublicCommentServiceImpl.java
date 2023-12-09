package ru.practicum.explore_with_me.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.exceptions.NotFoundException;
import ru.practicum.explore_with_me.mapper.CommentMapper;
import ru.practicum.explore_with_me.repository.CommentRepository;
import ru.practicum.explore_with_me.service.interfaces.PublicCommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicCommentServiceImpl implements PublicCommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventValidation eventValidation;
    @Override
    public List<CommentDto> getAllEventComments(Long eventId, int from, int size) {
        eventValidation.findEvent(eventId);
        return commentRepository.findAllByEventId(eventId).stream()
                .skip(from)
                .limit(size)
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getComment(Long eventId, Long commentId) {
        eventValidation.findEvent(eventId);
        return commentMapper.toCommentDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment id = " + commentId + " is not found.")));
    }
}
