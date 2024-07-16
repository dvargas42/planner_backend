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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.dvargas42.planner_backend.module.participant.dto.ParticipantConfirmReqDTO;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantConfirmRespDTO;
import com.dvargas42.planner_backend.module.participant.dto.ParticipantGetAllRespDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateReqDTO;
import com.dvargas42.planner_backend.module.trip.dto.TripCreateRespDTO;
import com.dvargas42.planner_backend.utils.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ParticipantControllerConfirm {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldToBeAbleToConfirmParticipant() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@gmail.com.br",
            "OWNERNAME OWNERLASTNAME");

        ResponseEntity<String> responseCreate = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        TripCreateRespDTO tripResponse = objectMapper.readValue(
            responseCreate.getBody(),
            TripCreateRespDTO.class
        );

        ResponseEntity<String> responseList = restTemplate.getForEntity(
            "/participants/?tripId=" + tripResponse.tripId().toString(),
            String.class
        );
        List<ParticipantGetAllRespDTO> participantList = objectMapper.readValue(
            responseList.getBody(),
            new TypeReference<List<ParticipantGetAllRespDTO>>() {}
        );
        ParticipantGetAllRespDTO participant = participantList.get(0);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParticipantConfirmReqDTO participantConfirmReqDTO = new ParticipantConfirmReqDTO(
            "PARTICIPANT NAME",
            "participant@gmail.com"
        );

        HttpEntity<ParticipantConfirmReqDTO> requestParticipantConfirm = new HttpEntity<>(
            participantConfirmReqDTO,
            headers
        );

        ResponseEntity<String> responseConfirm = restTemplate.exchange(
            "/participants/" + participant.id().toString(),
            HttpMethod.PATCH,
            requestParticipantConfirm,
            String.class
        );
        
        ParticipantConfirmRespDTO participantConfirm = objectMapper.readValue(
            responseConfirm.getBody(), 
            ParticipantConfirmRespDTO.class
        );

        assertEquals(200, responseConfirm.getStatusCode().value());
        assertEquals(participant.id(), participantConfirm.id());
        assertEquals("PARTICIPANT NAME", participantConfirm.name());
        assertEquals(participant.email(), participantConfirm.email());
        assertEquals(true, participantConfirm.is_confirmed());
    }

    @Test
    public void shouldToBeNotAbleToConfirmParticipantWhenIdIsInvalid() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParticipantConfirmReqDTO participantConfirmReqDTO = new ParticipantConfirmReqDTO(
            "PARTICIPANTNAME",
            "participant@gmail.com"
        );

        HttpEntity<ParticipantConfirmReqDTO> requestParticipantConfirm = new HttpEntity<>(
            participantConfirmReqDTO,
            headers
        );

        ResponseEntity<String> responseConfirm = restTemplate.exchange(
            "/participants/" + UUID.randomUUID().toString(),
            HttpMethod.PATCH,
            requestParticipantConfirm,
            String.class
        );
        
        assertEquals(400, responseConfirm.getStatusCode().value());
    }

    @Test
    public void shouldToBeNotAbleToConfirmParticipantWhenNameIsOne() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@gmail.com.br",
            "OWNERNAME OWNERLASTNAME");

        ResponseEntity<String> responseCreate = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        TripCreateRespDTO tripResponse = objectMapper.readValue(
            responseCreate.getBody(),
            TripCreateRespDTO.class
        );

        ResponseEntity<String> responseList = restTemplate.getForEntity(
            "/participants/?tripId=" + tripResponse.tripId().toString(),
            String.class
        );
        List<ParticipantGetAllRespDTO> participantList = objectMapper.readValue(
            responseList.getBody(),
            new TypeReference<List<ParticipantGetAllRespDTO>>() {}
        );
        ParticipantGetAllRespDTO participant = participantList.get(0);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParticipantConfirmReqDTO participantConfirmReqDTO = new ParticipantConfirmReqDTO(
            "PARTICIPANTNAME",
            "participant@gmail.com"
        );

        HttpEntity<ParticipantConfirmReqDTO> requestParticipantConfirm = new HttpEntity<>(
            participantConfirmReqDTO,
            headers
        );

        ResponseEntity<String> responseConfirm = restTemplate.exchange(
            "/participants/" + participant.id().toString(),
            HttpMethod.PATCH,
            requestParticipantConfirm,
            String.class
        );
        
        assertEquals(400, responseConfirm.getStatusCode().value());
    }

    @Test
    public void shouldToBeNotAbleToConfirmParticipantWhenNameIsInvalid() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@gmail.com.br",
            "OWNERNAME OWNERLASTNAME");

        ResponseEntity<String> responseCreate = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        TripCreateRespDTO tripResponse = objectMapper.readValue(
            responseCreate.getBody(),
            TripCreateRespDTO.class
        );

        ResponseEntity<String> responseList = restTemplate.getForEntity(
            "/participants/?tripId=" + tripResponse.tripId().toString(),
            String.class
        );
        List<ParticipantGetAllRespDTO> participantList = objectMapper.readValue(
            responseList.getBody(),
            new TypeReference<List<ParticipantGetAllRespDTO>>() {}
        );
        ParticipantGetAllRespDTO participant = participantList.get(0);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParticipantConfirmReqDTO participantConfirmReqDTO = new ParticipantConfirmReqDTO(
            "PARTICIPANT N@ME",
            "participant@gmail.com"
        );

        HttpEntity<ParticipantConfirmReqDTO> requestParticipantConfirm = new HttpEntity<>(
            participantConfirmReqDTO,
            headers
        );

        ResponseEntity<String> responseConfirm = restTemplate.exchange(
            "/participants/" + participant.id().toString(),
            HttpMethod.PATCH,
            requestParticipantConfirm,
            String.class
        );
        
        assertEquals(400, responseConfirm.getStatusCode().value());
    }

    @Test
    public void shouldToBeNotAbleToConfirmParticipantWhenEmailIsInvalid() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@gmail.com.br",
            "OWNERNAME OWNERLASTNAME");

        ResponseEntity<String> responseCreate = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        TripCreateRespDTO tripResponse = objectMapper.readValue(
            responseCreate.getBody(),
            TripCreateRespDTO.class
        );

        ResponseEntity<String> responseList = restTemplate.getForEntity(
            "/participants/?tripId=" + tripResponse.tripId().toString(),
            String.class
        );
        List<ParticipantGetAllRespDTO> participantList = objectMapper.readValue(
            responseList.getBody(),
            new TypeReference<List<ParticipantGetAllRespDTO>>() {}
        );
        ParticipantGetAllRespDTO participant = participantList.get(0);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParticipantConfirmReqDTO participantConfirmReqDTO = new ParticipantConfirmReqDTO(
            "PARTICIPANT NAME",
            "participantgmail.com"
        );

        HttpEntity<ParticipantConfirmReqDTO> requestParticipantConfirm = new HttpEntity<>(
            participantConfirmReqDTO,
            headers
        );

        ResponseEntity<String> responseConfirm = restTemplate.exchange(
            "/participants/" + participant.id().toString(),
            HttpMethod.PATCH,
            requestParticipantConfirm,
            String.class
        );
        
        assertEquals(400, responseConfirm.getStatusCode().value());
    }

    @Test
    public void shouldToBeNotAbleToConfirmParticipantWhenNameIsBlank() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@gmail.com.br",
            "OWNERNAME OWNERLASTNAME");

        ResponseEntity<String> responseCreate = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        TripCreateRespDTO tripResponse = objectMapper.readValue(
            responseCreate.getBody(),
            TripCreateRespDTO.class
        );

        ResponseEntity<String> responseList = restTemplate.getForEntity(
            "/participants/?tripId=" + tripResponse.tripId().toString(),
            String.class
        );
        List<ParticipantGetAllRespDTO> participantList = objectMapper.readValue(
            responseList.getBody(),
            new TypeReference<List<ParticipantGetAllRespDTO>>() {}
        );
        ParticipantGetAllRespDTO participant = participantList.get(0);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParticipantConfirmReqDTO participantConfirmReqDTO = new ParticipantConfirmReqDTO(
            "",
            "participantgmail.com"
        );

        HttpEntity<ParticipantConfirmReqDTO> requestParticipantConfirm = new HttpEntity<>(
            participantConfirmReqDTO,
            headers
        );

        ResponseEntity<String> responseConfirm = restTemplate.exchange(
            "/participants/" + participant.id().toString(),
            HttpMethod.PATCH,
            requestParticipantConfirm,
            String.class
        );
        
        assertEquals(400, responseConfirm.getStatusCode().value());
    }

    @Test
    public void shouldToBeNotAbleToConfirmParticipantWhenEmailIsBlank() throws Exception {
        List<String> emailList = new ArrayList<>();
        emailList.add("participant@gmail.com");

        LocalDateTime nowPlusOne = LocalDateTime.now().plusDays(1);
        LocalDateTime nowPlusTwo = LocalDateTime.now().plusDays(2);

        TripCreateReqDTO tripRequest = new TripCreateReqDTO(
            "LOCALTEST, TS",
            TestUtils.convertDateTime(nowPlusOne),
            TestUtils.convertDateTime(nowPlusTwo),
            emailList,
            "owner@gmail.com.br",
            "OWNERNAME OWNERLASTNAME");

        ResponseEntity<String> responseCreate = restTemplate.postForEntity(
                "/trips/", 
                tripRequest, 
                String.class
        );
        TripCreateRespDTO tripResponse = objectMapper.readValue(
            responseCreate.getBody(),
            TripCreateRespDTO.class
        );

        ResponseEntity<String> responseList = restTemplate.getForEntity(
            "/participants/?tripId=" + tripResponse.tripId().toString(),
            String.class
        );
        List<ParticipantGetAllRespDTO> participantList = objectMapper.readValue(
            responseList.getBody(),
            new TypeReference<List<ParticipantGetAllRespDTO>>() {}
        );
        ParticipantGetAllRespDTO participant = participantList.get(0);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParticipantConfirmReqDTO participantConfirmReqDTO = new ParticipantConfirmReqDTO(
            "PARTICIPANT NAME",
            ""
        );

        HttpEntity<ParticipantConfirmReqDTO> requestParticipantConfirm = new HttpEntity<>(
            participantConfirmReqDTO,
            headers
        );

        ResponseEntity<String> responseConfirm = restTemplate.exchange(
            "/participants/" + participant.id().toString(),
            HttpMethod.PATCH,
            requestParticipantConfirm,
            String.class
        );
        
        assertEquals(400, responseConfirm.getStatusCode().value());
    } 
}