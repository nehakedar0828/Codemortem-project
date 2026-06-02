package com.codemortem.controller;

import com.codemortem.dto.IncidentRequestDTO;
import com.codemortem.dto.IncidentResponseDTO;
import com.codemortem.dto.SimilarIncidentDTO;
import com.codemortem.entity.Incident;
import com.codemortem.enums.Severity;
import com.codemortem.enums.Status;
import com.codemortem.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@Tag(
      name = "Incident Management",
      description = "APIs for managing production incidents and AI analysis"
)
public class InidentController {

    private final IncidentService incidentService;

    public InidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    //Get all incidents
    @GetMapping
    @Operation(
            summary = "Get all incidents",
            description = "Returns all incidents belonging to the authenticated user"
    )
    public ResponseEntity<List<IncidentResponseDTO>> getAllIncidents(){
        return ResponseEntity.ok(
                incidentService.getAllIncidents());
    }

    //Get incident by id
    @GetMapping("/{id}")
    @Operation(
            summary = "Get incident by ID",
            description = "Returns a specific incident belonging to the authenticated user"
    )
    public Incident getIncidentById(@PathVariable Long id){
        return incidentService.getIncidentById(id);
    }

    //create incident
    @PostMapping
    @Operation(
            summary = "Create new incident",
            description = "Creates a production incident for the authenticated user"
    )
    public ResponseEntity<IncidentResponseDTO> createIncident(@Valid @RequestBody IncidentRequestDTO dto){

        IncidentResponseDTO response = incidentService.createIncident(dto);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    //delete incident
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete incident",
            description = "Deletes an incident belonging to the authenticated user"
    )
    public void deleteIncident(@PathVariable Long id){
        incidentService.deleteIncident(id);
    }

    //update incident
    @PutMapping("/{id}")
    @Operation(
            summary = "Update incident",
            description = "Updates an existing incident"
    )
    public Incident updateIncident(
            @PathVariable Long id,
            @RequestBody Incident incident){

        return incidentService.updateIncident(id,incident);
    }

    //for severity and status get request
    @GetMapping("/severity/{severity}")
    @Operation(
            summary = "Filter incidents by severity",
            description = "Returns incidents filtered by severity level"
    )
    public List<Incident> getIncidentsBySeverity(
            @PathVariable Severity severity){

        return incidentService.getIncidentBySeverity(severity);
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Filter incidents by status",
            description = "Returns incidents filtered by current status"
    )
    public List<Incident> getIncidentsByStatus(
            @PathVariable Status status){

        return incidentService.getIncidentByStatus(status);
    }

    //for pages
    @GetMapping("/paginated")
    @Operation(
            summary = "Get paginated incidents",
            description = "Returns incidents with pagination support"
    )
    public ResponseEntity<Page<IncidentResponseDTO>> getPaginatedIncidents(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size){
        //RequestParam is for reading query parameters
        return ResponseEntity.ok(
                incidentService.getPaginatedIncidents(
                        page,
                        size));
    }


    //for sorting
    @GetMapping("/sorted")
    @Operation(
            summary = "Get sorted incidents",
            description = "Returns incidents sorted by a specific field"
    )
    public ResponseEntity<List<IncidentResponseDTO>> getSortedIncidents(
            @RequestParam(defaultValue = "id") String sortBy){

        return ResponseEntity.ok(
                incidentService.getSortedIncidents(sortBy));
    }

    //for search
    @GetMapping("/search")
    @Operation(
            summary = "Search incidents",
            description = "Search incidents by title or description keywords"
    )
    public ResponseEntity<List<IncidentResponseDTO>> searchIncidents(

            @RequestParam String keyword){

        return ResponseEntity.ok(
                incidentService.searchIncidents(keyword));
    }

    //for similar incidents mapping
    @GetMapping("/similar")
    @Operation(
            summary = "Find similar incidents",
            description = "Finds incidents with similar textual content"
    )
    public ResponseEntity<List<SimilarIncidentDTO>> findSimilarIncidents(
            @RequestParam String text
    ){
        return ResponseEntity.ok(
                incidentService.findSimilarIncidents(text));
    }

    @PostMapping("{id}/analyze")
    @Operation(
            summary = "Analyze incident using AI",
            description = "Generates AI-powered debugging and mitigation suggestions for an incident"
    )
    public ResponseEntity<IncidentResponseDTO> analyzeIncident(
            @PathVariable Long id
    ){

        return ResponseEntity.ok(
                incidentService.analyzeIncident(id)
        );
    }


}
