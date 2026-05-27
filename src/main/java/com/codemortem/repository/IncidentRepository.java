package com.codemortem.repository;

import com.codemortem.entity.Incident;
import com.codemortem.enums.Severity;
import com.codemortem.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findBySeverity(Severity severity);

    List<Incident> findByStatus(Status status);

    List<Incident> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword,
            String descriptionKeyword
    );

    List<Incident> findByReportedByEmail(String email);
}