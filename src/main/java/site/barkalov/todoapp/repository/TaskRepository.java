package site.barkalov.todoapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import site.barkalov.todoapp.entity.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

}
