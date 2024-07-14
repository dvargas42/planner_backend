package com.dvargas42.planner_backend.module.trip;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dvargas42.planner_backend.module.trip.dto.TripCreateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateRespDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripGetDetailsRespDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripUpdateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripUpdateRespDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/trips")
@Tag(name = "Trip", description = "Trip information")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping("/")
    @Operation(summary = "Trip register", description = "This functionality is responsible for registering a trip")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = TripCreateRespDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Trip already exists")
    })
    public ResponseEntity<TripCreateRespDTO> createTrip(
            @RequestBody @Valid TripCreateReqDTO payload) {
        TripCreateRespDTO trip = this.tripService.createTrip(payload);

        return ResponseEntity.ok(trip);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trip get details", description = "This functionality is responsible for get details a trip")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = TripGetDetailsRespDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Trip is not found")
    })
    public ResponseEntity<TripGetDetailsRespDTO> getTripDetails(
            @PathVariable @NotNull UUID id) {
        TripGetDetailsRespDTO trip = this.tripService.getTripDetails(id);

        return ResponseEntity.ok(trip);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Trip update", description = "This functionality is responsible for update a trip")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = TripUpdateRespDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Trip not found")
    })
    public ResponseEntity<TripUpdateRespDTO> updateTrip(
            @PathVariable @NotNull UUID id, @RequestBody @Valid TripUpdateReqDTO payload) {
        TripUpdateRespDTO trip = this.tripService.updateTrip(id, payload);

        return ResponseEntity.ok(trip);
    }
}
