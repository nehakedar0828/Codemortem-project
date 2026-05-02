package com.codemortem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tags")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Incident> incidents;
}
