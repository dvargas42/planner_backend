package com.dvargas42.planner_backend.module.participant;

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

import com.dvargas42.planner_backend.module.participant.dto.ParticipantGetAllRespDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateRespDTO;
import com.dvargas42.planner_backend.utils.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ParticipantControllerGetAll {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldToBeAbleToGetAllParticipants() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant01@gmail.com");
        emailList.add("participant02@gmail.com");
        emailList.add("participant03@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@gmail.com.br",
            "OWNERNAME OWNERLASTNAME");

         ResponseEntity<String> responsePost = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        TripCreateRespDTO tripResponse = objectMapper.readValue(
            responsePost.getBody(),
            TripCreateRespDTO.class
        );

        ResponseEntity<String> responseGet = restTemplate.getForEntity(
            "/participants/?tripId=" + tripResponse.tripId().toString(),
            String.class
        );

        List<ParticipantGetAllRespDTO> participantList = objectMapper.readValue(
            responseGet.getBody(),
            new TypeReference<List<ParticipantGetAllRespDTO>>() {}
        );

        ParticipantGetAllRespDTO participant = participantList.get(2);
        

        assertEquals(200, responseGet.getStatusCode().value());
        assertEquals(3, participantList.size());
        assertEquals("", participant.name());
        assertEquals("participant03@gmail.com", participant.email());
        assertEquals(false, participant.is_confirmed());
    }

    @Test
    void shouldToBeNotAbleToGetAllParticipantsWhenTripIdIsNotExists() throws Exception {
        ResponseEntity<String> responseGet = restTemplate.getForEntity(
            "/participants/?tripId=" + UUID.randomUUID(),
            String.class
        );

        List<ParticipantGetAllRespDTO> participantList = objectMapper.readValue(
            responseGet.getBody(),
            new TypeReference<List<ParticipantGetAllRespDTO>>() {}
        );
        
        assertEquals(200, responseGet.getStatusCode().value());
        assertEquals(0, participantList.size());
    }
}
