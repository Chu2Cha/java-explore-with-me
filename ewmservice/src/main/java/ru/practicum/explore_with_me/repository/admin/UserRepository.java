package ru.practicum.explore_with_me.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findAllByIdIn(long[] ids);
}
