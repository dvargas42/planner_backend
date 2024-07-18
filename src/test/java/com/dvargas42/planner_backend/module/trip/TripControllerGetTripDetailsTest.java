package com.dvargas42.planner_backend.module.trip;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.dvargas42.planner_backend.module.trip.dto.TripCreateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateRespDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripGetDetailsRespDTO;
import com.dvargas42.planner_backend.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TripControllerGetTripDetailsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final String participantEmail01 = "participant01@email.com";
    private final String participantEmail02 = "participant02@email.com";
    private final String participantEmail03 = "participant03@email.com";
    private final String destination = "localTest, LT";
    private final String ownerName = "firstName lastName";
    private final String ownerEmail = "owner@email.com";

    private final String startsAt = TestUtils.convertDateTime(LocalDateTime.now().plusDays(1));
    private final String endsAt = TestUtils.convertDateTime(LocalDateTime.now().plusDays(2));


    @Test
    void shouldToBeAbleToGetAllTripDetails() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add(participantEmail01);
        emailList.add(participantEmail02);
        emailList.add(participantEmail03);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            destination,
            startsAt,
            endsAt,
            emailList,
            ownerEmail,
            ownerName
        );

        ResponseEntity<String> responsePost = restTemplate.postForEntity(
            "/trips/", 
            tripRequest, 
            String.class
        );
        TripCreateRespDTO tripResponseCreate = objectMapper
                .readValue(responsePost.getBody(), TripCreateRespDTO.class);
        

        ResponseEntity<String> responseGet = restTemplate.getForEntity(
            "/trips/" + tripResponseCreate.tripId().toString(),  
            String.class
        );
        TripGetDetailsRespDTO tripResponseGet = objectMapper.readValue(responseGet.getBody(), TripGetDetailsRespDTO.class);

        assertEquals(200, responseGet.getStatusCode().value());
        assertEquals(7, TestUtils.getDeclaratedFields(TripGetDetailsRespDTO.class));

        assertThat(tripResponseGet.id()).isInstanceOf(UUID.class);
        assertEquals(destination, tripResponseGet.destination());
        assertEquals(startsAt.substring(0, 20), tripResponseGet.starts_at().toString().substring(0, 20));
        assertEquals(endsAt.substring(0, 20), tripResponseGet.ends_at().toString().substring(0, 20));
        assertEquals(false, tripResponseGet.is_confirmed());
        assertEquals(ownerName, tripResponseGet.owner_name());
        assertEquals(ownerEmail, tripResponseGet.owner_email());
    }

    @Test
    void shouldToBeNotAbleGetAllTripDetailsWhenIdIsIncorrect() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add(participantEmail01);
        emailList.add(participantEmail02);
        emailList.add(participantEmail03);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            destination,
            startsAt,
            endsAt,
            emailList,
            ownerEmail,
            ownerName
        );

        ResponseEntity<String> responsePost = restTemplate.postForEntity(
            "/trips/", 
            tripRequest, 
            String.class
        );
        TripCreateRespDTO tripResponseCreate = objectMapper
                .readValue(responsePost.getBody(), TripCreateRespDTO.class);
        

        ResponseEntity<String> responseGet = restTemplate.getForEntity(
            "/trips/" + tripResponseCreate.tripId().toString().substring(5),  
            String.class
        );

        assertEquals(400, responseGet.getStatusCode().value());
    }
}
