package com.team20.backend.service;

import com.team20.backend.model.Venue;
import com.team20.backend.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VenueServiceImp implements VenueService{

    @Autowired
    private VenueRepository venueRepository;
    @Override
    public Venue findByVenueId(int id) {
        return venueRepository.findByVenueId(id);
    }

    @Override
    public Venue findByName(String name) {
        return venueRepository.findByName(name);
    }
}
