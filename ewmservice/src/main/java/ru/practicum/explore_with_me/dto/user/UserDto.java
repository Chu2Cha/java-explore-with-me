package ru.practicum.explore_with_me.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank
    @Email
    private String email;

    private Long id;

    @NotBlank
    private String name;

}
