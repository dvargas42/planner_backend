package com.dvargas42.planner_backend.module.link;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dvargas42.planner_backend.module.link.dto.LinkCreateReqDTO;
import com.dvargas42.planner_backend.module.link.dto.LinkCreateRespDTO;
import com.dvargas42.planner_backend.module.link.dto.LinkGetAllRespDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/links")
@Tag(name = "Link", description = "Link information")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @PostMapping("/")
    @Operation(summary = "Link register", description = "This functionality is responsible for create a link to an trip")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = LinkCreateRespDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Trip not found")
    })
    public ResponseEntity<LinkCreateRespDTO> createLink(@RequestBody @Valid LinkCreateReqDTO payload) {
        
        LinkCreateRespDTO link = this.linkService.createLinkToEvent(payload);

        return ResponseEntity.ok(link);
    }

    @GetMapping("/")
    @Operation(summary = "Link search all", description = "This functionality is responsible for get all links of a trip")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = LinkGetAllRespDTO.class))
        }),
        @ApiResponse(responseCode = "400", description = "Trip not found")
    })
    public ResponseEntity<List<LinkGetAllRespDTO>> getAllLinks(@PathVariable @NotNull UUID tripId) {

        List<LinkGetAllRespDTO> linkList = this.linkService.getAllLinksFromEvent(tripId);

        return ResponseEntity.ok(linkList);
    }
}
