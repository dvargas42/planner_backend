package com.dvargas42.planner_backend.module.participant;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvargas42.planner_backend.exception.ParticipantNotFoundException;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantInviteRespDTO;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantGetAllRespDTO;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantInviteReqDTO;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantConfirmReqDTO;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantConfirmRespDTO;
import com.dvargas42.planner_backend.module.trip.Trip;
import com.dvargas42.planner_backend.module.trip.TripService;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private TripService tripService;

    public List<ParticipantGetAllRespDTO> getAllParticipantsFromEvent(UUID tripId) {

        return this.participantRepository.findByTripId(tripId).stream()
                .map(participant -> new ParticipantGetAllRespDTO(
                        participant.getId(),
                        participant.getName(),
                        participant.getEmail(),
                        participant.getIsConfirmed()))
                .toList();
    }

    public ParticipantConfirmRespDTO comfirmParticipantToEvent(
            UUID participantId, ParticipantConfirmReqDTO payload) {
                
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> {
                    throw new ParticipantNotFoundException("id", participantId.toString());
                });

        participant.setIsConfirmed(true);
        participant.setName(payload.name());
        this.participantRepository.save(participant);

        return new ParticipantConfirmRespDTO(participant);
    }

    public ParticipantInviteRespDTO inviteParticipanteToEvent(
            ParticipantInviteReqDTO payload) {

        Trip trip = tripService.findTrip(payload.trip_id());
        var participant = this.createParticipantToEvent(payload.email(), trip);

        if (!trip.getIsConfirmed()) {
            this.triggerConfirmationEmailToParticipant(payload.email());
        }

        return new ParticipantInviteRespDTO(participant.getId());
    }

    public Participant createParticipantToEvent(String email, Trip trip) {

        Participant participant = new Participant(email, trip);
        this.participantRepository.save(participant);

        return participant;
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {
        //TODO implement this
    }

    public void triggerConfirmationEmailToParticipant(String email) {
        //TODO implement this
    }
}
