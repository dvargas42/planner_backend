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

    @Test
    void shouldToBeAbleToGetAllTripDetails() throws Exception {
        String DESTINATION = "LOCALTEST, TS";
        String OWNER_EMAIL = "owner@gmail.com.br";
        String OWNER_NAME = "OWNERFIRSTNAME OWNERLASTNAME";

        List<String> EMAIL_LIST = new ArrayList<>();
        EMAIL_LIST.add("participant01@gmail.com");
        EMAIL_LIST.add("participant02@gmail.com");
        EMAIL_LIST.add("participant03@gmail.com");

        String STARTS_AT = TestUtils.convertDateTime(LocalDateTime.now().plusDays(1));
        String ENDS_AT = TestUtils.convertDateTime(LocalDateTime.now().plusDays(2));

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            DESTINATION,
            STARTS_AT,
            ENDS_AT,
            EMAIL_LIST,
            OWNER_EMAIL,
            OWNER_NAME
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
        assertEquals(DESTINATION, tripResponseGet.destination());
        assertEquals(STARTS_AT.substring(0, 20), tripResponseGet.starts_at().toString().substring(0, 20));
        assertEquals(ENDS_AT.substring(0, 20), tripResponseGet.ends_at().toString().substring(0, 20));
        assertEquals(false, tripResponseGet.is_confirmed());
        assertEquals(OWNER_NAME, tripResponseGet.owner_name());
        assertEquals(OWNER_EMAIL, tripResponseGet.owner_email());
    }

    @Test
    void shouldToBeNotAbleGetAllTripDetailsWhenIdIsIncorrect() throws Exception {
        String DESTINATION = "LOCALTEST, TS";
        String OWNER_EMAIL = "owner@gmail.com.br";
        String OWNER_NAME = "OWNERFIRSTNAME OWNERLASTNAME";

        List<String> EMAIL_LIST = new ArrayList<>();
        EMAIL_LIST.add("participant01@gmail.com");
        EMAIL_LIST.add("participant02@gmail.com");
        EMAIL_LIST.add("participant03@gmail.com");

        String STARTS_AT = TestUtils.convertDateTime (LocalDateTime.now().plusDays(1));
        String ENDS_AT = TestUtils.convertDateTime(LocalDateTime.now().plusDays(2));

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            DESTINATION,
            STARTS_AT,
            ENDS_AT,
            EMAIL_LIST,
            OWNER_EMAIL,
            OWNER_NAME
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
