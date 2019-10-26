package site.barkalov.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.barkalov.todoapp.entity.Task;
import site.barkalov.todoapp.exception.RecordNotFoundException;
import site.barkalov.todoapp.service.TaskService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class TaskController {

    @Autowired
    TaskService service;

    @RequestMapping
    public String getAllTasks(Model model)
    {
        List<Task> list = service.getAllTasks();

        model.addAttribute("tasks", list);
        return "list-tasks";
    }

    @RequestMapping("/done")
    public String getAllDoneTasks(Model model) {
        List<Task> list = service.getAllTasks().stream()
                .filter(Task::isStatus)
                .collect(Collectors.toList());

        model.addAttribute("tasks", list);
        return "list-tasks";
    }

    @RequestMapping("/inprogress")
    public String getAllInProgressTasks(Model model) {
        List<Task> list = service.getAllTasks().stream()
                .filter(task -> !task.isStatus())
                .collect(Collectors.toList());

        model.addAttribute("tasks", list);
        return "list-tasks";
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String editTaskById(Model model, @PathVariable("id") Optional<Long> id)
            throws RecordNotFoundException
    {
        if (id.isPresent()) {
            Task entity = service.getTaskById(id.get());
            model.addAttribute("task", entity);
        } else {
            model.addAttribute("task", new Task());
        }
        return "add-edit-task";
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteTaskById(Model model, @PathVariable("id") Long id)
            throws RecordNotFoundException
    {
        service.deleteTaskById(id);
        return "redirect:/";
    }

    @RequestMapping(path = "/createTask", method = RequestMethod.POST)
    public String createOrUpdateTask(Task task) throws RecordNotFoundException {
        if(task.getTitle().isEmpty()) {
            throw new RecordNotFoundException("Tittle is empty");
        }
        service.createOrUpdateTask(task);
        return "redirect:/";
    }
}
