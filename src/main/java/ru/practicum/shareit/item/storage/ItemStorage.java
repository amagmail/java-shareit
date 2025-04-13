package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {

    @Query("select i from Item i " +
            "where i.available = true " +
            "and (upper(i.name) like upper(concat('%', ?1, '%')) or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> getSearch(String text);

    Collection<Item> findAllByOwner(Long ownerId);

    @Query(value = "insert into comments(text, item_id, author_id) " +
            "values(:#{#entity.text}, :#{#entity.item}, :#{#entity.author}) " +
            "returning *", nativeQuery = true)
    Comment addComment(Comment entity);

}
