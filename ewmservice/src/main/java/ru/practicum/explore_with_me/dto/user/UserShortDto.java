package ru.practicum.explore_with_me.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {

    @NotBlank
    private Long id;

    @NotBlank
    private String name;
}
