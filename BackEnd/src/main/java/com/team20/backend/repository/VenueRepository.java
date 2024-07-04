package com.team20.backend.repository;

import com.team20.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team20.backend.model.Venue;
public interface VenueRepository extends JpaRepository<Venue, Integer> {
    Venue findByVenueId(int id);

    Venue findByName(String name);
}
