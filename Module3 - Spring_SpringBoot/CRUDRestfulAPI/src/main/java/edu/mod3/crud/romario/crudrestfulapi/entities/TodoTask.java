package edu.mod3.crud.romario.crudrestfulapi.entities;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TodoTask {

    private int id;

    private String title;

    private String description;

}