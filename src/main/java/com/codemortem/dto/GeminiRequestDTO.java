package com.codemortem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GeminiRequestDTO {

    private List<Content> contents;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Content{

        private List<Part> parts;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Part{

        private String text;
    }
}
