package com.example.server_system.service;

import com.example.server_system.dto.ProjectDto;
import com.example.server_system.mapper.ProjectMapper;
import com.example.server_system.models.Project;
import com.example.server_system.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public ProjectDto createProject(ProjectDto dto) {
        Project project = projectMapper.toEntity(dto);
        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toDto)
                .toList();
    }

    public Optional<ProjectDto> getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(projectMapper::toDto);
    }

    public Optional<ProjectDto> deleteProject(Long id) {
        Optional<Project> projectOpt = projectRepository.findById(id);
        if (projectOpt.isPresent()) {
            projectRepository.deleteById(id);
            return projectOpt.map(projectMapper::toDto);
        }
        return Optional.empty();
    }
}
