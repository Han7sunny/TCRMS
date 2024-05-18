package com.kutca.tcrms.event.repository;

import com.kutca.tcrms.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByEventIdBetween(Long startEventId, Long endEventId);
}
