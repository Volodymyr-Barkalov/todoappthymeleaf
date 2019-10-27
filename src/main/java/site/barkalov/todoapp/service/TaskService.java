package site.barkalov.todoapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.barkalov.todoapp.entity.Task;
import site.barkalov.todoapp.exception.RecordNotFoundException;
import site.barkalov.todoapp.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    TaskRepository repository;

    public List<Task> getAllTasks() {
        List<Task> result = (List<Task>) repository.findAll();

        if (result.size() > 0) {
            return result;
        } else {
            return new ArrayList<Task>();
        }
    }

    public Task getTaskById(Long id) throws RecordNotFoundException {
        Optional<Task> task = repository.findById(id);

        if (task.isPresent()) {
            return task.get();
        } else {
            throw new RecordNotFoundException("No task record exist for given id");
        }
    }

    public Task createOrUpdateTask(Task entity) {
        if (entity.getId() == null) {
            entity.setDate(LocalDateTime.now());
            entity = repository.save(entity);

            return entity;
        } else {
            Optional<Task> task = repository.findById(entity.getId());
            if (task.isPresent()) {
                Task newEntity = task.get();
                newEntity.setDate(LocalDateTime.now());
                newEntity.setTitle(entity.getTitle());
                newEntity.setDescription(entity.getDescription());
                newEntity.setStatus(entity.isStatus());
                newEntity = repository.save(newEntity);
                return newEntity;
            } else {
                entity = repository.save(entity);
                return entity;
            }
        }
    }

    public void deleteTaskById(Long id) throws RecordNotFoundException {
        Optional<Task> task = repository.findById(id);
        if (task.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No task record exist for given id");
        }
    }
}
