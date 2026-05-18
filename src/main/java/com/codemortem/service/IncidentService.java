package com.codemortem.service;

import com.codemortem.dto.IncidentRequestDTO;
import com.codemortem.dto.IncidentResponseDTO;
import com.codemortem.entity.Incident;
import com.codemortem.enums.Severity;
import com.codemortem.enums.Status;
import com.codemortem.exception.ResourceNotFoundException;
import com.codemortem.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

//actual business logic here : controller -> service -> repository -> database
@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;


    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    //Create
    public IncidentResponseDTO createIncident(
            IncidentRequestDTO requestDTO){
        Incident incident = new Incident();

        incident.setTitle(requestDTO.getTitle());
        incident.setDescription(requestDTO.getDescription());
        incident.setSeverity(requestDTO.getSeverity());
        incident.setStatus(requestDTO.getStatus());
        incident.setAffectedService(requestDTO.getAffectedService());

        Incident savedIncident = incidentRepository.save(incident);

        return mapToResponseDTO(savedIncident);
    }

    //Read one
    public Incident getIncidentById(Long id){
        return incidentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Incident not found"));
    }

    //read all
    public List<IncidentResponseDTO> getAllIncidents(){
        return incidentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    //delete
    public void deleteIncident(Long id){
        incidentRepository.deleteById(id);
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
                .build();
    }

}
