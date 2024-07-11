package com.dvargas42.planner_backend.module.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvargas42.planner_backend.module.trip.Trip;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponse saveActivity(ActivityRequestPayload payload, Trip trip) {
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);
        this.activityRepository.save(newActivity);
        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData> getAllActivitiesFromEvent(UUID tripId) {
        return this.activityRepository.findByTripId(tripId).stream().map(
                activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())
        ).toList();
    }
}
