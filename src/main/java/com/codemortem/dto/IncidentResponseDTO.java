package com.codemortem.dto;

import com.codemortem.enums.Severity;
import com.codemortem.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IncidentResponseDTO {

    private Long id;

    private String title;

    private String description;

    private Severity severity;

    private Status status;

    private String affectedService;
}
