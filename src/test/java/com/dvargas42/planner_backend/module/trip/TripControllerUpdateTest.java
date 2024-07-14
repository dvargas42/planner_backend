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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.dvargas42.planner_backend.module.trip.dto.TripCreateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateRespDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripUpdateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripUpdateRespDTO;
import com.dvargas42.planner_backend.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TripControllerUpdateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldToBeAbleToUpdate() throws Exception {
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
        

        String DESTINATION_UPDATE = "LOCALTEST UPDATE, TS";
        String STARTS_AT_UPDATE = TestUtils.convertDateTime(LocalDateTime.now().plusDays(2));
        String ENDS_AT_UPDATE = TestUtils.convertDateTime(LocalDateTime.now().plusDays(4));

        TripUpdateReqDTO tripUpdateReqDTO = new TripUpdateReqDTO(
            DESTINATION_UPDATE,
            STARTS_AT_UPDATE,
            ENDS_AT_UPDATE
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TripUpdateReqDTO> requestEntity = new HttpEntity<>(tripUpdateReqDTO, headers);

        ResponseEntity<String> responsePatch = restTemplate.exchange(
            "/trips/" + tripResponseCreate.tripId().toString(),
            HttpMethod.PATCH,
            requestEntity,
            String.class
        );

        TripUpdateRespDTO tripResponseUpdate = objectMapper.readValue(responsePatch.getBody(), TripUpdateRespDTO.class);

        assertEquals(200, responsePatch.getStatusCode().value());

        assertThat(tripResponseUpdate.id()).isInstanceOf(UUID.class);
        assertEquals(DESTINATION_UPDATE, tripResponseUpdate.destination());
        assertEquals(STARTS_AT_UPDATE.substring(0, 20), tripResponseUpdate.starts_at().toString().substring(0, 20));
        assertEquals(ENDS_AT_UPDATE.substring(0, 20), tripResponseUpdate.ends_at().toString().substring(0, 20));
        assertEquals(false, tripResponseUpdate.is_confirmed());
        assertEquals(OWNER_NAME, tripResponseUpdate.owner_name());
        assertEquals(OWNER_EMAIL, tripResponseUpdate.owner_email());
    }

    @Test
    public void shouldToBeNotAbleToUpdatWhenStartsAtInPast() throws Exception {
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
        

        String DESTINATION_UPDATE = "LOCALTEST UPDATE, TS";
        String STARTS_AT_UPDATE = TestUtils.convertDateTime(LocalDateTime.now().minusDays(2));
        String ENDS_AT_UPDATE = TestUtils.convertDateTime(LocalDateTime.now().plusDays(4));

        TripUpdateReqDTO tripUpdateReqDTO = new TripUpdateReqDTO(
            DESTINATION_UPDATE,
            STARTS_AT_UPDATE,
            ENDS_AT_UPDATE
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TripUpdateReqDTO> requestEntity = new HttpEntity<>(tripUpdateReqDTO, headers);

        ResponseEntity<String> responsePatch = restTemplate.exchange(
            "/trips/" + tripResponseCreate.tripId().toString(),
            HttpMethod.PATCH,
            requestEntity,
            String.class
        );

        assertEquals(400, responsePatch.getStatusCode().value());
    }

    @Test
    public void shouldToBeNotAbleToUpdatWhenEndsAtLessThanStartsAt() throws Exception {
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
        

        String DESTINATION_UPDATE = "LOCALTEST UPDATE, TS";
        String STARTS_AT_UPDATE = TestUtils.convertDateTime(LocalDateTime.now().plusDays(2));
        String ENDS_AT_UPDATE = TestUtils.convertDateTime(LocalDateTime.now().plusDays(1));

        TripUpdateReqDTO tripUpdateReqDTO = new TripUpdateReqDTO(
            DESTINATION_UPDATE,
            STARTS_AT_UPDATE,
            ENDS_AT_UPDATE
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TripUpdateReqDTO> requestEntity = new HttpEntity<>(tripUpdateReqDTO, headers);

        ResponseEntity<String> responsePatch = restTemplate.exchange(
            "/trips/" + tripResponseCreate.tripId().toString(),
            HttpMethod.PATCH,
            requestEntity,
            String.class
        );

        assertEquals(400, responsePatch.getStatusCode().value());
    }

    @Test
    public void shouldToBeNotAbleToUpdatWhenStartsAtIsEqualEndsAt() throws Exception {
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
        

        String DESTINATION_UPDATE = "LOCALTEST UPDATE, TS";
        String STARTS_AT_UPDATE = TestUtils.convertDateTime(LocalDateTime.now().plusDays(2));
        String ENDS_AT_UPDATE = STARTS_AT_UPDATE;

        TripUpdateReqDTO tripUpdateReqDTO = new TripUpdateReqDTO(
            DESTINATION_UPDATE,
            STARTS_AT_UPDATE,
            ENDS_AT_UPDATE
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TripUpdateReqDTO> requestEntity = new HttpEntity<>(tripUpdateReqDTO, headers);

        ResponseEntity<String> responsePatch = restTemplate.exchange(
            "/trips/" + tripResponseCreate.tripId().toString(),
            HttpMethod.PATCH,
            requestEntity,
            String.class
        );

        assertEquals(400, responsePatch.getStatusCode().value());
    }
    
}
