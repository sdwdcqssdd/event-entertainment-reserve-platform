package com.team20.backend.service;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.team20.backend.model.Venue;
import com.team20.backend.repository.VenueRepository;
import com.team20.backend.service.VenueServiceImp;
/**
 * AI-generated-content
 * tool: chatGpt
 * version: 4.0
 * usage: gpt根据我给的测试模板为其他方法写测试
 */
@ExtendWith(MockitoExtension.class)
public class VenueServiceImpTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueServiceImp venueService;

    @Test
    public void findByVenueId_ShouldReturnVenue() {
        Venue expectedVenue = new Venue();
        expectedVenue.setVenueId(1);
        expectedVenue.setName("Conference Hall");
        expectedVenue.setCapacity(200);

        given(venueRepository.findByVenueId(1)).willReturn(expectedVenue);

        Venue result = venueService.findByVenueId(1);

        assertThat(result).isEqualTo(expectedVenue);
        assertThat(result.getName()).isEqualTo("Conference Hall");
        assertThat(result.getCapacity()).isEqualTo(200);
    }

    @Test
    public void findByName_ShouldReturnVenue() {
        Venue expectedVenue = new Venue();
        expectedVenue.setVenueId(1);
        expectedVenue.setName("Banquet Hall");
        expectedVenue.setCapacity(300);

        given(venueRepository.findByName("Banquet Hall")).willReturn(expectedVenue);

        Venue result = venueService.findByName("Banquet Hall");

        assertThat(result).isEqualTo(expectedVenue);
        assertThat(result.getVenueId()).isEqualTo(1);
        assertThat(result.getCapacity()).isEqualTo(300);
    }
}

