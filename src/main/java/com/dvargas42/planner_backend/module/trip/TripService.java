package com.dvargas42.planner_backend.module.trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvargas42.planner_backend.exception.TripEndsAtIsNotMoreThanStartsAtException;
import com.dvargas42.planner_backend.exception.TripNotFoundException;
import com.dvargas42.planner_backend.exception.TripStartsAtEqualEndsAtException;
import com.dvargas42.planner_backend.exception.TripStartsAtInPastException;
import com.dvargas42.planner_backend.module.participant.Participant;
import com.dvargas42.planner_backend.module.participant.ParticipantRepository;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateRespDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripGetDetailsRespDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripUpdateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripUpdateRespDTO;

@Service
public class TripService {
    
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    public TripCreateRespDTO createTrip(TripCreateReqDTO payload) {
        Trip newTrip = new Trip(payload);
        
        if (!newTrip.isStartsAtAfterNow()) {
            throw new TripStartsAtInPastException(
                "starts_at", newTrip.getStartsAt().toString());
        }

        if (newTrip.isStartsAtEqualsEndsAt()) {
            throw new TripStartsAtEqualEndsAtException(
                "starts_at", newTrip.getStartsAt().toString(), 
                "ends_at", newTrip.getEndsAt().toString());
        }

        if (!newTrip.isEndsAtMoreThanStartsAt()) {
            throw new TripEndsAtIsNotMoreThanStartsAtException(
                "starts_at", newTrip.getStartsAt().toString(), 
                "ends_at", newTrip.getEndsAt().toString());
        }

        this.tripRepository.save(newTrip);
        this.createParticipantsToEvent(payload.emails_to_invite(), newTrip);
        
        return new TripCreateRespDTO(newTrip.getId());
    }

    public TripGetDetailsRespDTO getTripDetails(UUID id) {
        Trip trip = this.findTrip(id);
        
        return new TripGetDetailsRespDTO(trip);
    }

    public TripUpdateRespDTO updateTrip(UUID id, TripUpdateReqDTO payload) {
        Trip trip = this.findTrip(id);
        trip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
        trip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
        trip.setDestination(payload.destination());

        this.tripRepository.save(trip);

        return new TripUpdateRespDTO(trip);
    }

    public Trip findTrip(UUID id) {
        return this.tripRepository.findById(id)
            .orElseThrow(() -> {
                throw new TripNotFoundException("id", id.toString());
            });

    }

    private void createParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream()
                .map(email -> new Participant(email, trip)).toList();

        this.participantRepository.saveAll(participants);
    }
}
