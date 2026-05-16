package com.codemortem.service;

import com.codemortem.entity.Incident;
import com.codemortem.enums.Severity;
import com.codemortem.enums.Status;
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
    public Incident createIncident(Incident incident){
        return incidentRepository.save(incident);
    }

    //Read one
    public Incident getIncidentById(Long id){
        return incidentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Incident not found"));
    }

    //read all
    public List<Incident> getAllIncidents(){
        return incidentRepository.findAll();
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

}
