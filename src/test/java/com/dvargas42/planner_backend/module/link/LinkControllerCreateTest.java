package com.dvargas42.planner_backend.module.link;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.dvargas42.planner_backend.module.link.dto.LinkCreateReqDTO;
import com.dvargas42.planner_backend.module.link.dto.LinkCreateRespDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateRespDTO;
import com.dvargas42.planner_backend.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LinkControllerCreateTest {
    
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
    void shouldToBeAbleToCreateLink() throws JsonMappingException, JsonProcessingException {
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
        TripCreateRespDTO tripResponse = objectMapper.readValue(response.getBody(), TripCreateRespDTO.class);

        LinkCreateReqDTO linkRequest = new LinkCreateReqDTO(
            tripResponse.tripId(),
            "Link Test Title",
            "http://www.link-test.com.br"
        );

        ResponseEntity<String> responseCreateLink = restTemplate.postForEntity(
            "/links/", 
            linkRequest, 
            String.class
        );
        LinkCreateRespDTO linkResponse = objectMapper.readValue(responseCreateLink.getBody(), LinkCreateRespDTO.class);

        assertEquals(200, responseCreateLink.getStatusCode().value());
        assertThat(linkResponse.id()).isInstanceOf(UUID.class);
    }

    @Test
    void shouldToBeNotAbleToCreateLinkWhenTripIdIsNotFound() {
        LinkCreateReqDTO linkRequest = new LinkCreateReqDTO(
            UUID.randomUUID(),
            "Link Test Title",
            "http://www.link-test.com.br"
        );

        ResponseEntity<String> responseCreateLink = restTemplate.postForEntity(
            "/links/", 
            linkRequest, 
            String.class
        );

        assertEquals(400, responseCreateLink.getStatusCode().value());
    }

    @ParameterizedTest
    @CsvSource({
        "Link Test Title, testemail.com",
        "Link Test Title, test@@email.com",
        "Link Test Title, \" \"",
        "Link Test Title, \"\"",
        "Link Test Title,",
        "Link@ Test     , test@email.com",
        "Link           , test@email.com",
        "\" \"          , test@email.com",
        "\"\"           , test@email.com",
        "               , test@email.com",
    })
    void shouldToBeAbleToCreateLinkWhenTripIdTitleUrlAreInvalid(
        String title,
        String url
    ) throws JsonMappingException, JsonProcessingException {
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
        TripCreateRespDTO tripResponse = objectMapper.readValue(response.getBody(), TripCreateRespDTO.class);

        LinkCreateReqDTO linkRequest = new LinkCreateReqDTO(
            tripResponse.tripId(),
            "Link Test Title",
            "http://www.link-test.com.br"
        );

        ResponseEntity<String> responseCreateLink = restTemplate.postForEntity(
            "/links/", 
            linkRequest, 
            String.class
        );

        assertEquals(200, responseCreateLink.getStatusCode().value());
    }
}
