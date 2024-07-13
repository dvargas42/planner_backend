package com.dvargas42.planner_backend.module.link;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvargas42.planner_backend.module.link.dto.LinkGetAllRespDTO;
import com.dvargas42.planner_backend.module.link.dto.LinkCreateReqDTO;
import com.dvargas42.planner_backend.module.link.dto.LinkCreateRespDTO;
import com.dvargas42.planner_backend.module.trip.Trip;
import com.dvargas42.planner_backend.module.trip.TripService;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private TripService tripService;

    public LinkCreateRespDTO createLinkToEvent(LinkCreateReqDTO payload) {

        Trip trip = tripService.findTrip(payload.trip_id());
        Link newLink = new Link(payload.title(), payload.url(), trip);

        this.linkRepository.save(newLink);

        return new LinkCreateRespDTO(newLink.getId());
    }

    public List<LinkGetAllRespDTO> getAllLinksFromEvent(UUID id) {
        
        return this.linkRepository.findByTripId(id).stream()
            .map(link -> new LinkGetAllRespDTO(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
