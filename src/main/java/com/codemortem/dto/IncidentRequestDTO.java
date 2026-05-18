package com.codemortem.dto;

import com.codemortem.enums.Severity;
import com.codemortem.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidentRequestDTO {

    @NotBlank(message = "Title is Required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Severity is required")
    private Severity severity;

    @NotNull(message = "Status is required")
    private Status status;

    @NotBlank(message = "Affected service is required")
    private String affectedService;
}
