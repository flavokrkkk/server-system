package com.mono.service;

import com.mono.dto.ProjectDto;
import com.mono.mapper.ProjectMapper;
import com.mono.models.Notification;
import com.mono.models.Project;
import com.mono.models.User;
import com.mono.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final EmailService emailService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, EmailService emailService, UserService userService, NotificationService notificationService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.emailService = emailService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public ProjectDto createProject(ProjectDto dto) {
        Project project = projectMapper.toEntity(dto);


        Project savedProject = projectRepository.save(project);


        User currentUser = userService.getCurrentUser();


        String subject = "Создание проекта";
        String message = "Проект \"" + savedProject.getName() + "\" был успешно создан. " +
                "Описание: \"" + savedProject.getDescription() + "\". " +
                "ID проекта: " + savedProject.getId();


        emailService.sendEmail(currentUser.getEmail(), subject, message);


        Notification notification = new Notification();
        notification.setUser(currentUser);
        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setRead(false);
        notificationService.saveNotification(notification);

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
