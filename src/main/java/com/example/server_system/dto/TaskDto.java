package com.example.server_system.dto;

import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String name;
    private String description;
}