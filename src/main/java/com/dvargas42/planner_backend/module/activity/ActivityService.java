package com.dvargas42.planner_backend.module.activity;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvargas42.planner_backend.module.activity.dto.ActivityGetAllRespDTO;
import com.dvargas42.planner_backend.module.activity.dto.ActivityCreateReqDTO;
import com.dvargas42.planner_backend.module.activity.dto.ActivityCreateRespDTO;
import com.dvargas42.planner_backend.module.trip.Trip;
import com.dvargas42.planner_backend.module.trip.TripService;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TripService tripService;

    public ActivityCreateRespDTO createActivity(ActivityCreateReqDTO payload) {

        Trip trip = this.tripService.findTrip(payload.trip_id());
        Activity activity = new Activity(payload.title(), payload.occurs_at(), trip);

        this.activityRepository.save(activity);

        return new ActivityCreateRespDTO(activity.getId());
    }

    public List<ActivityGetAllRespDTO> getAllActivitiesFromEvent(UUID tripId) {
        
        return this.activityRepository.findByTripId(tripId).stream()
                .map(ActivityGetAllRespDTO::new).toList();
    }
}
