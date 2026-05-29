package com.codemortem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OpenRouterRequestDTO {

    private String model;

    private List<Message> messages;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message {

        private String role;

        private String content;
    }
}