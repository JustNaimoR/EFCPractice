package edu.mod5.crud.romario.crudrestfulapi.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "todo_table")
public class TodoTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    // Обновление значений в текущем таске на значения переданного
    public void update(TodoTask task) {
        id = task.id;
        title = task.title;
        description = task.description;
    }
}