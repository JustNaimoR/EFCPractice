create table todo_table (
    id int PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    title varchar not null,
    description varchar null
)