package com.dvargas42.planner_backend.module.activity;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dvargas42.planner_backend.module.activity.dto.ActivityCreateReqDTO;
import com.dvargas42.planner_backend.module.activity.dto.ActivityCreateRespDTO;
import com.dvargas42.planner_backend.module.activity.dto.ActivityGetAllRespDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/activities")
@Tag(name = "Activity", description = "Activity information")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    
    @PostMapping("/")
    @Operation(summary = "Activity register", description = "This functionality is responsible for create a activity to an trip")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ActivityCreateRespDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Trip not found")
    })
    public ResponseEntity<ActivityCreateRespDTO> createActivity(
            @RequestBody @Valid ActivityCreateReqDTO payload) {

        ActivityCreateRespDTO activity = this.activityService.createActivity(payload);

        return ResponseEntity.ok(activity);
    }
    
    @GetMapping("/")
    @Operation(summary = "Activity search all", description = "This functionality is responsible for get all activities of a trip")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ActivityGetAllRespDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Trip not found")
    })
    public ResponseEntity<List<ActivityGetAllRespDTO>> getAllActivities(@RequestParam @NotNull UUID tripId) {

        List<ActivityGetAllRespDTO> activityList = this.activityService
                .getAllActivitiesFromEvent(tripId);

        return ResponseEntity.ok(activityList);
    }
}
