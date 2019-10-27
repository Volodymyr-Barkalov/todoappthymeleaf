package site.barkalov.todoapp.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import site.barkalov.todoapp.entity.Task;
import site.barkalov.todoapp.exception.RecordNotFoundException;
import site.barkalov.todoapp.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    private LocalDateTime creationTime = LocalDateTime.now();
    private final Task givenTask =
            new Task(1L, "Clean", "Description", creationTime, true);

    @InjectMocks
    private TaskService service;

    @Mock
    private TaskRepository repository;

    @Test
    public void should_get_all_tasks() {
        //given
        List<Task> expectedResult = Arrays.asList(givenTask);
        when(repository.findAll()).thenReturn(expectedResult);
        //when
        List<Task> actualResult = service.getAllTasks();
        //then
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSameSizeAs(expectedResult);
        assertThat(actualResult).containsSequence(expectedResult);
    }

    @Test
    public void should_get_all_tasks_return_empty_list() {
        //given
        List<Task> expectedResult = emptyList();
        when(repository.findAll()).thenReturn(expectedResult);
        //when
        List<Task> actualResult = service.getAllTasks();
        //then
        assertThat(actualResult).isEmpty();
    }



    @Test
    public void should_get_task_by_id() throws RecordNotFoundException {
        //given
        when(repository.findById(1L)).thenReturn(Optional.of(givenTask));
        //when
        Task actualResult = service.getTaskById(1L);
        //then
        assertThat(actualResult).isEqualTo(givenTask);
    }

    @Test
    public void should_thrown_exception_when_get_task_by_id() throws RecordNotFoundException {
        //given
        when(repository.findById(1L)).thenReturn(Optional.empty());
        //when, then
        assertThatThrownBy(() -> service.getTaskById(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("No task record exist for given id");
    }

    @Test
    public void should_create_task() {
        //given
        Task newTask = new Task(null,"Clean", "Description", creationTime, true);
        when(repository.save(newTask)).thenReturn(givenTask);
        //when
        Task actualResult = service.createOrUpdateTask(newTask);
        //then
        assertThat(actualResult).isEqualTo(givenTask);
    }

    @Test
    public void should_update_task() {
        //given
        Task updatedTask = new Task(1L,"Learn", "Learn Java", LocalDateTime.now(), true);
        when(repository.findById(1L)).thenReturn(Optional.of(updatedTask));
        when(repository.save(updatedTask)).thenReturn(updatedTask);
        //when
        Task actualResult = service.createOrUpdateTask(givenTask);
        //then
        assertThat(actualResult).isEqualTo(updatedTask);
    }

    @Test
    public void should_save_task_when_not_found_in_DB() {
        //given
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(repository.save(givenTask)).thenReturn(givenTask);
        //when
        Task actualResult = service.createOrUpdateTask(givenTask);
        //then
        assertThat(actualResult).isEqualTo(givenTask);
    }

    @Test
    public void should_delete_task_by_id() throws RecordNotFoundException {
        //given
        when(repository.findById(1L)).thenReturn(Optional.of(givenTask));
        doNothing().when(repository).deleteById(1L);
        //when
        service.deleteTaskById(1L);
        //then
        verify(repository).deleteById(1L);
    }

    @Test
    public void should_throw_exception_when_delete_task_by_id() throws RecordNotFoundException {
        //given
        when(repository.findById(1L)).thenReturn(Optional.empty());
        //when, then
        assertThatThrownBy(() -> service.deleteTaskById(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("No task record exist for given id");
    }
}