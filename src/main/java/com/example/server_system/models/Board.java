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
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Связь с карточками
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Card> cards = new ArrayList<>();
}