package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String authorName;
    private LocalDateTime created;

    @NotBlank(message = "Текст комментария не может быть пустым")
    private String text;

}
