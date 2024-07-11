package com.dvargas42.planner_backend.module.link;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dvargas42.planner_backend.module.trip.Trip;
import com.dvargas42.planner_backend.module.trip.TripService;

@RestController
@RequestMapping("/links")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private TripService tripService;

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> createLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
            Trip trip = tripService.findTrip(id);

            LinkResponse linkResponse = this.linkService.saveLink(payload, trip);

            return ResponseEntity.ok(linkResponse);
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
        List<LinkData> linkList = this.linkService.getAllLinksFromEvent(id);

        return ResponseEntity.ok(linkList);
    }

    
}
