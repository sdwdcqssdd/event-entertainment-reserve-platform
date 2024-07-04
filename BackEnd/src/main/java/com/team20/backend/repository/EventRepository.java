package com.team20.backend.repository;

import com.team20.backend.dto.EventQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team20.backend.model.Event;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Event findByEventId(int eventId);
    List<Event> findByDate(Date date);

    List<Event> findByType(Event.EventType type);

//    @Query("SELECT e FROM Event e " +
//            "WHERE (:#{#query.dates} IS NULL OR e.date IN :#{#query.dates}) " +
//            "AND (:#{#query.venueIds} IS NULL OR e.venueId IN :#{#query.venueIds}) " +
//            "AND (:#{#query.organizerIds} IS NULL OR e.organizerId IN :#{#query.organizerIds}) " +
//            "AND (:#{#query.keywords} IS NULL OR e.description LIKE CONCAT('%', :#{#query.keywords}, '%'))")
//    List<Event> findByQuery(@Param("query") EventQuery query);

    @Query("SELECT e FROM Event e " +
            "WHERE (:#{#query.dates} IS NULL OR e.date IN :#{#query.dates}) " +
            "AND (:#{#query.venueIds} IS NULL OR e.venueId IN :#{#query.venueIds}) " +
            "AND (:#{#query.categoryIds} IS NULL OR e.categoryId IN :#{#query.categoryIds}) " +
            "AND (:#{#query.organizerIds} IS NULL OR e.organizerId IN :#{#query.organizerIds}) " +
            "AND (:#{#query.keyword} IS NULL OR e.description LIKE CONCAT('%', :#{#query.keyword}, '%'))")
    List<Event> findByQuery(EventQuery query);


}

