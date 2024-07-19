package com.dvargas42.planner_backend.module.link;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.dvargas42.planner_backend.module.link.dto.LinkCreateReqDTO;
import com.dvargas42.planner_backend.module.link.dto.LinkGetAllRespDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateRespDTO;
import com.dvargas42.planner_backend.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LinkControllerGetAllTest {
        
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
        TripCreateRespDTO tripResponse = objectMapper.readValue(
            response.getBody(), TripCreateRespDTO.class);

        LinkCreateReqDTO linkRequest;

        linkRequest = new LinkCreateReqDTO(
            tripResponse.tripId(),
            "Link Test Title 01",
            "http://www.link01.com.br"
        );
        restTemplate.postForEntity(
            "/links/", 
            linkRequest, 
            String.class
        );

        linkRequest = new LinkCreateReqDTO(
            tripResponse.tripId(),
            "Link Test Title 02",
            "http://www.link02.com.br"
        );
        restTemplate.postForEntity(
            "/links/", 
            linkRequest, 
            String.class
        );

        linkRequest = new LinkCreateReqDTO(
            tripResponse.tripId(),
            "Link Test Title 03",
            "http://www.link03.com.br"
        );
        restTemplate.postForEntity(
            "/links/", 
            linkRequest, 
            String.class
        );

        ResponseEntity<String> responseGet = restTemplate.getForEntity(
            "/links/?tripId=" + tripResponse.tripId(),  
            String.class
        );
        List<LinkGetAllRespDTO> LinkGetResponse = objectMapper.readValue(
            responseGet.getBody(),  
            new TypeReference<List<LinkGetAllRespDTO>>() {}
        );

        assertEquals(200, responseGet.getStatusCode().value());
        assertEquals(3, LinkGetResponse.size());
    }

    @ParameterizedTest
    @CsvSource({
        "b5664e21-d2bb-4a72",
        "\" \"",
        "\"\""
    })
    void shouldToBeNotAbleToCreateLinkWhenTripIdIsInvalid(String tripId) {
        ResponseEntity<String> responseGet = restTemplate.getForEntity(
            "/links/?tripId=" + tripId,  
            String.class
        );

        assertEquals(400, responseGet.getStatusCode().value());
    }
    
    @Test
    void shouldToBeNotAbleToCreateLinkWhenTripIdInvalid() throws JsonMappingException, JsonProcessingException {
        ResponseEntity<String> responseGet = restTemplate.getForEntity(
            "/links/?tripId=b5664e21-d2bb-4a72-9ac5-a7e9dcaf331f",  
            String.class
        );

        List<LinkGetAllRespDTO> LinkGetResponse = objectMapper.readValue(
            responseGet.getBody(),  
            new TypeReference<List<LinkGetAllRespDTO>>() {}
        );

        assertEquals(200, responseGet.getStatusCode().value());
        assertEquals(0, LinkGetResponse.size());
    }
}
