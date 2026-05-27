package com.codemortem.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.codemortem.enums.Severity;
import com.codemortem.enums.Status;


@Entity
@Table(name = "incidents")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String affectedService;

    private LocalDateTime occurredAt;

    private LocalDateTime resolvedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "reported_by_id")
    private User reportedBy;

    @OneToOne(mappedBy = "incident")
    private RootCause rootCause;

    @ManyToMany
    @JoinTable(
            name = "incident_tags",
            joinColumns = @JoinColumn(name = "incident_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
}
