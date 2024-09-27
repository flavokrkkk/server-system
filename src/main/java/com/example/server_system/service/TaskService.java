package com.example.server_system.service;

import com.example.server_system.dto.TaskDto;
import com.example.server_system.mapper.TaskMapper;
import com.example.server_system.models.Task;
import com.example.server_system.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .toList();
    }

    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    public Optional<TaskDto> getTaskById(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDto);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
