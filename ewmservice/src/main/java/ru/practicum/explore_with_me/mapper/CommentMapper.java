package ru.practicum.explore_with_me.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.comment.CommentDto;
import ru.practicum.explore_with_me.model.Comment;

@Component
@AllArgsConstructor
public class CommentMapper {
    public CommentDto toCommentDto (Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .eventId(comment.getEvent().getId())
                .commentDate(comment.getCommentDate())
                .edited(comment.getEdited())
                .text(comment.getText())
                .build();
    }

}
