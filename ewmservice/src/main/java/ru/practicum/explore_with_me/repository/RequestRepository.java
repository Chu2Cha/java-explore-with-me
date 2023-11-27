package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.enums.RequestStatus;
import ru.practicum.explore_with_me.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findOneByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findOneByRequesterIdAndId(Long userId, Long requestId);

    @Modifying
    @Query("UPDATE Request r SET r.status = :status WHERE r.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") RequestStatus status);


}
