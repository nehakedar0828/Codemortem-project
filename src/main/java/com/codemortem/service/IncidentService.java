package com.codemortem.service;

import com.codemortem.dto.AIAnalysisResponseDTO;
import com.codemortem.dto.IncidentRequestDTO;
import com.codemortem.dto.IncidentResponseDTO;
import com.codemortem.dto.SimilarIncidentDTO;
import com.codemortem.entity.Incident;
import com.codemortem.entity.User;
import com.codemortem.enums.Severity;
import com.codemortem.enums.Status;
import com.codemortem.exception.ResourceNotFoundException;
import com.codemortem.repository.IncidentRepository;
import com.codemortem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

//actual business logic here : controller -> service -> repository -> database
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    private final AuthService authService;

    private final UserRepository userRepository;

    private final AIAnalysisService aiAnalysisService;

    //Create
    public IncidentResponseDTO createIncident(
            IncidentRequestDTO requestDTO){
        Incident incident = new Incident();

        incident.setTitle(requestDTO.getTitle());
        incident.setDescription(requestDTO.getDescription());
        incident.setSeverity(requestDTO.getSeverity());
        incident.setStatus(requestDTO.getStatus());
        incident.setAffectedService(requestDTO.getAffectedService());

        String email = authService.getCurrentEmail();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        incident.setReportedBy(user);

        Incident savedIncident = incidentRepository.save(incident);

        return mapToResponseDTO(savedIncident);
    }

    //Read one
    public Incident getIncidentById(Long id){

        String email = authService.getCurrentEmail();

        Incident incident = incidentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Incident not found"));

        if(!incident.getReportedBy()
                .getEmail()
                .equals(email)){

            throw new RuntimeException("Access Denied");

        }

        return incident;

    }

    //read all
    public List<IncidentResponseDTO> getAllIncidents(){

        String email = authService.getCurrentEmail();

        return incidentRepository
                .findByReportedByEmail(email)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    //delete
    public void deleteIncident(Long id){

        String email =
                authService.getCurrentEmail();

        Incident incident = incidentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Incident not found"));

        if(!incident.getReportedBy()
                .getEmail()
                .equals(email)) {

            throw new RuntimeException(
                    "Access denied");
        }

        incidentRepository.delete(incident);
    }

    //filter by severity
    public List<Incident> getIncidentBySeverity(Severity severity){
        return incidentRepository.findBySeverity(severity);
    }

    //filter by status
    public List<Incident> getIncidentByStatus(Status status){
        return incidentRepository.findByStatus(status);
    }

    //update
    public Incident updateIncident(Long id,Incident updateIncident){

        Incident existingIncident = incidentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Incident not found"));

        existingIncident.setTitle(updateIncident.getTitle());
        existingIncident.setDescription(updateIncident.getDescription());
        existingIncident.setSeverity(updateIncident.getSeverity());
        existingIncident.setStatus(updateIncident.getStatus());
        existingIncident.setAffectedService(updateIncident.getAffectedService());

        return incidentRepository.save(existingIncident);
    }

    //for getting responseDTO instead of incident
    private IncidentResponseDTO mapToResponseDTO(
            Incident incident) {

        return IncidentResponseDTO.builder()
                .id(incident.getId())
                .title(incident.getTitle())
                .description(incident.getDescription())
                .severity(incident.getSeverity())
                .status(incident.getStatus())
                .affectedService(
                        incident.getAffectedService())
                .aiAnalysis(incident.getAiAnalysis())
                .build();
    }

    //for scalable backend - handle 1000 incidents at a time
    public Page<IncidentResponseDTO> getPaginatedIncidents(int page,int size){

        Pageable pageable = PageRequest.of(page,size);

        return incidentRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    //to sort the responses according to specific condition like severity status etc.
    public List<IncidentResponseDTO> getSortedIncidents(String sortBy){

        return incidentRepository
                .findAll(Sort.by(sortBy))
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    //to search on particular title or description
    public List<IncidentResponseDTO> searchIncidents(String keyword){

        return incidentRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword,
                        keyword
                )
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    //to find similarity score and find similar incidents
    public List<SimilarIncidentDTO> findSimilarIncidents(String inputText){

        List<Incident> incidents = incidentRepository.findAll();

        List<SimilarIncidentDTO> results = new ArrayList<>();

        Set<String> inputWords = Arrays.stream(inputText.toLowerCase().split("\\s+"))
                .collect(Collectors.toSet());

        for(Incident incident : incidents){

            String combinedText =
                    (incident.getTitle()+ " " +
                            incident.getDescription())
                                    .toLowerCase();

            Set<String> incidentWords =
                    Arrays.stream(combinedText.split("\\s+"))
                            .collect(Collectors.toSet());

            Set<String> commonWords = new HashSet<>(inputWords);

            commonWords.retainAll(incidentWords);

            double score =
                    (double) commonWords.size()/inputWords.size();

            if(score > 0){

                results.add(
                        new SimilarIncidentDTO(
                                incident.getId(),
                                incident.getTitle(),
                                score
                        )
                );
            }
        }

        results.sort((a,b) ->
                Double.compare(
                        b.getSimilarityScore(),
                        a.getSimilarityScore()
                ));

        return results;
    }

    public IncidentResponseDTO analyzeIncident(
            Long id
    ){

        String email = authService.getCurrentEmail();

        Incident incident = incidentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Incident not found"));

        if(!incident.getReportedBy()
                .getEmail()
                .equals(email)){
            throw new RuntimeException("Unauthorized access");
        }

        AIAnalysisResponseDTO aiResponse =
                aiAnalysisService
                        .analyzeIncident(
                                incident.getTitle(),
                                incident.getDescription()
                        );

        incident.setAiAnalysis(
                aiResponse.getAnalysis());

        Incident savedIncident = incidentRepository.save(incident);

        return mapToResponseDTO(savedIncident);

    }

}
