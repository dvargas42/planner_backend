package com.dvargas42.planner_backend.module.trip;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
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

    private final String participantEmail01 = "participant01@email.com";
    private final String participantEmail02 = "participant02@email.com";
    private final String participantEmail03 = "participant03@email.com";
    private final String destination = "localTest, LT";
    private final String ownerName = "firstName lastName";
    private final String ownerEmail = "owner@email.com";

    @Test
    void shouldToBeAbleToCreateTrip() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add(participantEmail01);
        emailList.add(participantEmail02);
        emailList.add(participantEmail03);

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            destination,
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            ownerEmail,
            ownerName);

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(200, response.getStatusCode().value());

        TripCreateRespDTO tripResponse = objectMapper.readValue(response.getBody(), TripCreateRespDTO.class);
        assertThat(tripResponse.tripId()).isInstanceOf(UUID.class);
    }


    private static Stream<Arguments> provideDateInterval() {
        return Stream.of(
            Arguments.of(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(1)),
            Arguments.of(LocalDateTime.now(), LocalDateTime.now().plusDays(2)),
            Arguments.of(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDateInterval")
    void shouldToBeNotAbleToCreateTripWhereStartsAtAndEndsAtAreInvalid(
        LocalDateTime nowPlusOne,
        LocalDateTime nowPlusTwo
    ) {
        List<String> emailList = new ArrayList<>();
        emailList.add(participantEmail01);
        emailList.add(participantEmail02);
        emailList.add(participantEmail03);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            destination,
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            ownerEmail,
            ownerName);

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(400, response.getStatusCode().value());
    }

    @ParameterizedTest
    @CsvSource({
        "\"Loc@l Test LT\"  , FisrtName LastName , owner@email.com",
        "\" \"              , FisrtName LastName , owner@email.com",
        "\"\"               , FisrtName LastName , owner@email.com",
        "                   , FisrtName LastName , owner@email.com",
        
        "\"Local Test LT\"  , FirstName_LastName , owner@email.com",
        "\"Local Test LT\"  , FirstName L@stName , owner@email.com",
        "\"Local Test LT\"  , \" \"              , owner@email.com",
        "\"Local Test LT\"  , \"\"               , owner@email.com",
        "\"Local Test LT\"  ,                    , owner@email.com",

        "\"Local Test LT\", FirstName LastName  , owner@@email.com",
        "\"Local Test LT\", FirstName LastName  , owneremail.com",
        "\"Local Test LT\", FirstName LastName  , \" \"",
        "\"Local Test LT\", FirstName LastName  , \"\"",
        "\"Local Test LT\", FirstName LastName  ,",
    })
    void shouldToBeNotAbleToCreateTripWhereOwnerNameOwnerEmailAndDestinationAreInvalid(
            String destination, String ownerName, String ownerEmail) {
        List<String> emailList = new ArrayList<>();
        emailList.add(participantEmail01);
        emailList.add(participantEmail02);
        emailList.add(participantEmail03);

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            destination,
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            ownerEmail,
            ownerName);

         ResponseEntity<String> response = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        assertEquals(400, response.getStatusCode().value());
    }

}
