package com.codemortem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimilarIncidentDTO {

    private Long incidentId;

    private String title;

    private double similarityScore;

}
