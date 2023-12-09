package ru.practicum.explore_with_me.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestCommentDto {

     @NotBlank
    @Size(min = 10, max = 280)
    private String text;

}
