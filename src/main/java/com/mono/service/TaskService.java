package com.mono.service;

import com.mono.dto.TaskDto;
import com.mono.mapper.TaskMapper;
import com.mono.models.LoginCredentials;
import com.mono.models.Notification;
import com.mono.models.Task;
import com.mono.models.User;
import com.mono.repository.TaskRepository;
import com.mono.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final EmailService emailService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, EmailService emailService, UserService userService, UserRepo userRepository, User user, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.emailService = emailService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .toList();
    }

    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task savedTask = taskRepository.save(task);

        User currentUser = userService.getCurrentUser();
        String subject = "Добавление задачи";
        String message = "Задача \"" + savedTask.getName() + "\" была успешно добавлена. " +
                "Описание: \"" + savedTask.getDescription() + "\". " +
                "ID задачи: " + savedTask.getId();

        emailService.sendEmail(currentUser.getEmail(), subject, message);

        Notification notification = new Notification();
        notification.setUser(currentUser);
        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setRead(false);
        notificationService.saveNotification(notification);

        return taskMapper.toDto(savedTask);
    }

    public Optional<TaskDto> getTaskById(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDto);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
