package com.dvargas42.planner_backend.module.link;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvargas42.planner_backend.module.trip.Trip;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public LinkResponse saveLink(LinkRequestPayload payload, Trip trip) {
        Link newLink = new Link(payload.title(), payload.url(), trip);
        this.linkRepository.save(newLink);
        return new LinkResponse(newLink.getId());

    }

    public List<LinkData> getAllLinksFromEvent(UUID id) {
        return this.linkRepository.findByTripId(id).stream()
                .map(link -> new LinkData(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
