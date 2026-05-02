package com.codemortem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "root_causes")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RootCause {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String fixApplied;

    @OneToOne
    @JoinColumn(name = "incident_id",unique = true)
    private Incident incident;
}
