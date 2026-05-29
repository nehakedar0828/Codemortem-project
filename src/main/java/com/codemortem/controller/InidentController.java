package com.codemortem.controller;

import com.codemortem.dto.IncidentRequestDTO;
import com.codemortem.dto.IncidentResponseDTO;
import com.codemortem.dto.SimilarIncidentDTO;
import com.codemortem.entity.Incident;
import com.codemortem.enums.Severity;
import com.codemortem.enums.Status;
import com.codemortem.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class InidentController {

    private final IncidentService incidentService;

    public InidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    //Get all incidents
    @GetMapping
    public ResponseEntity<List<IncidentResponseDTO>> getAllIncidents(){
        return ResponseEntity.ok(
                incidentService.getAllIncidents());
    }

    //Get incident by id
    @GetMapping("/{id}")
    public Incident getIncidentById(@PathVariable Long id){
        return incidentService.getIncidentById(id);
    }

    //create incident
    @PostMapping
    public ResponseEntity<IncidentResponseDTO> createIncident(@Valid @RequestBody IncidentRequestDTO dto){

        IncidentResponseDTO response = incidentService.createIncident(dto);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    //delete incident
    @DeleteMapping("/{id}")
    public void deleteIncident(@PathVariable Long id){
        incidentService.deleteIncident(id);
    }

    //update incident
    @PutMapping("/{id}")
    public Incident updateIncident(
            @PathVariable Long id,
            @RequestBody Incident incident){

        return incidentService.updateIncident(id,incident);
    }

    //for severity and status get request
    @GetMapping("/severity/{severity}")
    public List<Incident> getIncidentsBySeverity(
            @PathVariable Severity severity){

        return incidentService.getIncidentBySeverity(severity);
    }

    @GetMapping("/status/{status}")
    public List<Incident> getIncidentsByStatus(
            @PathVariable Status status){

        return incidentService.getIncidentByStatus(status);
    }

    //for pages
    @GetMapping("/paginated")
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
    public ResponseEntity<List<IncidentResponseDTO>> getSortedIncidents(
            @RequestParam(defaultValue = "id") String sortBy){

        return ResponseEntity.ok(
                incidentService.getSortedIncidents(sortBy));
    }

    //for search
    @GetMapping("/search")
    public ResponseEntity<List<IncidentResponseDTO>> searchIncidents(

            @RequestParam String keyword){

        return ResponseEntity.ok(
                incidentService.searchIncidents(keyword));
    }

    //for similar incidents mapping
    @GetMapping("/similar")
    public ResponseEntity<List<SimilarIncidentDTO>> findSimilarIncidents(
            @RequestParam String text
    ){
        return ResponseEntity.ok(
                incidentService.findSimilarIncidents(text));
    }

    @PostMapping("{id}/analyze")
    public ResponseEntity<IncidentResponseDTO> analyzeIncident(
            @PathVariable Long id
    ){

        return ResponseEntity.ok(
                incidentService.analyzeIncident(id)
        );
    }


}
