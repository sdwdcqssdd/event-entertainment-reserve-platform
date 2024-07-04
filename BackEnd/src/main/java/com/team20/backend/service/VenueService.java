package com.team20.backend.service;

import com.team20.backend.model.Venue;

public interface VenueService {
    public Venue findByVenueId(int id);

    Venue findByName(String name);
}
