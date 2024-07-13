package com.dvargas42.planner_backend.module.participant;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dvargas42.planner_backend.module.participant.dto.ParticipantConfirmReqDTO;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantConfirmRespDTO;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantGetAllRespDTO;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantInviteReqDTO;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantInviteRespDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/participants")
@Tag(name = "Participant", description = "Participant information")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/")
    @Operation(summary = "Participant search all", description = "This functionality is responsible for get all participants of a trip")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ParticipantGetAllRespDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Trip not found")
    })
    public ResponseEntity<List<ParticipantGetAllRespDTO>> getAllParticipantsFromEvent(
            @RequestParam @NotNull UUID tripId) {
                
        List<ParticipantGetAllRespDTO> participantList = this.participantService
                .getAllParticipantsFromEvent(tripId);

        return ResponseEntity.ok(participantList);
    }

    @PatchMapping("/{participantId}")
    @Operation(summary = "Participant register", description = "This functionality is responsible for confirm a participant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ParticipantConfirmRespDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Trip not found")
    })
    public ResponseEntity<ParticipantConfirmRespDTO> comfirmParticipantToEvent(
            @PathVariable @NotNull UUID participantId,
            @RequestBody @Valid ParticipantConfirmReqDTO payload) {
    
        ParticipantConfirmRespDTO participant = this.participantService
                .comfirmParticipantToEvent(participantId, payload);

        return ResponseEntity.ok(participant);
    }

    @PostMapping("/invites")
    @Operation(summary = "Participant invite", description = "This functionality is responsible for invite a participant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ParticipantInviteRespDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Trip not found")
    })
    public ResponseEntity<ParticipantInviteRespDTO> inviteParticipantToEvent(
            @RequestBody @Valid ParticipantInviteReqDTO payload) {

        var participantId = this.participantService.inviteParticipanteToEvent(payload);

        return ResponseEntity.ok(participantId);
    }
}
