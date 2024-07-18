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

    private final String startAt = "starts_at";
    private final String endsAt = "ends_at";

    public TripCreateRespDTO createTrip(TripCreateReqDTO payload) {
        Trip trip = new Trip(payload);
        
        dateValidation(trip);

        this.tripRepository.save(trip);
        this.createParticipantsToEvent(payload.emails_to_invite(), trip);
        
        return new TripCreateRespDTO(trip.getId());
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

        dateValidation(trip);

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

    private void dateValidation(Trip trip) {
        if (!trip.isStartsAtAfterNow()) {
            throw new TripStartsAtInPastException(
                startAt, trip.getStartsAt().toString());
        }

        if (trip.isStartsAtEqualsEndsAt()) {
            throw new TripStartsAtEqualEndsAtException(
                startAt, trip.getStartsAt().toString(), 
                endsAt, trip.getEndsAt().toString());
        }

        if (!trip.isEndsAtMoreThanStartsAt()) {
            throw new TripEndsAtIsNotMoreThanStartsAtException(
                startAt, trip.getStartsAt().toString(), 
                endsAt, trip.getEndsAt().toString());
        }
    }
}
