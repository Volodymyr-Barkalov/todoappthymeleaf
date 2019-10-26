package site.barkalov.todoapp.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name="TASKS")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please fill the description")
    @Column(name="title", nullable=false, length=50)
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="creation_date", nullable=false, columnDefinition="TIMESTAMP")
    private LocalDateTime date;

    @Column(name="status", nullable = false)
    private boolean status;

}
