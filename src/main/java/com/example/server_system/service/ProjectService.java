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
    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper){
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public ProjectDto createProject(ProjectDto dto){
        Project project = projectMapper.toEntity(dto);
        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    public List<ProjectDto> getAllProject(){
        return projectRepository.findAll().stream().map(projectMapper::toDto).toList();
    }

    public Optional<ProjectDto> deleteProject(Long id){
        if(projectRepository.existsById(id)){
            Optional<Project> optionalProject = projectRepository.findById(id);

            Project project = optionalProject.orElseThrow(() -> new RuntimeException("Project not found"));

            projectRepository.deleteById(id);

            ProjectDto projectDto = projectMapper.toDto(project);

            if (projectDto == null) {
                throw new RuntimeException("Failed to map Project to ProjectDto");
            }

            return Optional.of(projectDto);
        } else {
            return Optional.empty();
        }
    }


    public Optional<ProjectDto> getProjectById(Long id){
        if(projectRepository.existsById(id)){
            Optional<Project> projects = projectRepository.findById(id);
            Project project = projects.orElseThrow(() -> new RuntimeException("Project not found"));
            ProjectDto projectDto = projectMapper.toDto(project);
            if (projectDto == null) {
                throw new RuntimeException("Failed to map Project to ProjectDto");
            }
            return Optional.of(projectDto);
        }else {
            return Optional.empty();
        }
    }
}