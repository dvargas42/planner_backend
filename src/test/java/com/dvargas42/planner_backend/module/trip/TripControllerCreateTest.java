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
import com.dvargas42.planner_backend.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TripControllerCreateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldToBeAbleToCreateTrip() throws Exception {
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

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(200, response.getStatusCode().value());

        TripCreateRespDTO tripResponse = objectMapper.readValue(response.getBody(), TripCreateRespDTO.class);
        assertThat(tripResponse.tripId()).isInstanceOf(UUID.class);
    }

    @Test
    void shouldToBeNotAbleToCreateTripWhereStartAtIsInPast() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant01@gmail.com");
        emailList.add("participant02@gmail.com");
        emailList.add("participant03@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now().minusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@gmail.com.br",
            "OWNERNAME OWNERLASTNAME");

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(400, response.getStatusCode().value());
    }


    @Test
    void shouldToBeNotAbleToCreateTripWhereStartAtEqualNow() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant01@gmail.com");
        emailList.add("participant02@gmail.com");
        emailList.add("participant03@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now();
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(1);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@gmail.com.br",
            "OWNERNAME OWNERLASTNAME");

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void shouldToBeNotAbleToCreateTripWhereEndsAtLessThanStartsAt() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant01@gmail.com");
        emailList.add("participant02@gmail.com");
        emailList.add("participant03@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now();

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@gmail.com.br",
            "OWNERNAME OWNERLASTNAME");

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void shouldToBeNotAbleToCreateTripWhereOwnerNullorBlank() throws Exception {
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
            null);

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void shouldToBeNotAbleToCreateTripWhereOwnerNameisOneWord() throws Exception {
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
            "OWNERNAME");

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void shouldToBeNotAbleToCreateTripWhereOwnerNameHaveEspecialChars() throws Exception {
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
            "OWNERFIRSTNAME OWNERLASTNAME@");

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(400, response.getStatusCode().value());
    }
    
    @Test
    void shouldToBeNotAbleToCreateTripWhereOwnerEmailNotValid() throws Exception {
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
            "owneremail",
            "OWNERFIRSTNAME OWNERLASTNAME");

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void shouldToBeNotAbleToCreateTripWhereDestinationHaveSpecialChars() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant01@gmail.com");
        emailList.add("participant02@gmail.com");
        emailList.add("participant03@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST_, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@email.com",
            "OWNERFIRSTNAME OWNERLASTNAME");

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(400, response.getStatusCode().value());
    }

}
