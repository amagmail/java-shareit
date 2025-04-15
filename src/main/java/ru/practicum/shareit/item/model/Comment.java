package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "created")
    private LocalDateTime created;

    @JoinColumn(name = "item_id", nullable = false)
    @ManyToOne
    @ToString.Exclude
    private Item item;

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne
    @ToString.Exclude
    private User author;

}
