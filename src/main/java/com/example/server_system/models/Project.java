package com.example.server_system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int priority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Связь с досками
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();
}
