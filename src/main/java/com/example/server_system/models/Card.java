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
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    //Связь с проектами
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;


    // Связь с задачами
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

}
