package edu.mod5.crud.romario.crudrestfulapi.repository;

import edu.mod5.crud.romario.crudrestfulapi.HibernateConfig;
import edu.mod5.crud.romario.crudrestfulapi.entities.TodoTask;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ToDoRepository {
//    private JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

//    public int saveAndRetrieveId(TodoTask task) {
//        String insertIntoSQL = "insert into todo_table(title, description) values (?, ?)";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection
//                    .prepareStatement(insertIntoSQL, new String[] {"id"});
//            ps.setString(1, task.getTitle());
//            ps.setString(2, task.getDescription());
//            return ps;
//        }, keyHolder);
//
//        return (int) keyHolder.getKey();
//    }

    public int saveAndRetrieveId(TodoTask task) {
        int id = -1;
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            id = (Integer) session.save(task);

            tx.commit();
        } catch (Exception exc) {
            if (tx != null)
                tx.rollback();
        }

        return id;
    }

//    public List<TodoTask> getAll() {
//        return jdbcTemplate.query("select * from todo_table", new BeanPropertyRowMapper<>(TodoTask.class));
//    }
//    @Transactional
    public List<TodoTask> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from TodoTask", TodoTask.class).getResultList();
        }
    }

//    public void update(int id, TodoTask task) {
//        jdbcTemplate.update("update todo_table set title = ?, description = ? where id = ?",
//                task.getTitle(), task.getDescription(), id);
//    }
    public void update(int id, TodoTask task) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            TodoTask old = session.get(TodoTask.class, id);
            old.update(task);
            session.merge(old);

            tx.commit();
        } catch (Exception exc) {
            if (tx != null)
                tx.rollback();
        }
    }

//    public void remove(int id) {
//        jdbcTemplate.update("delete from todo_table where id = ?", id);
//    }
    public void remove(int id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            TodoTask task = session.get(TodoTask.class, id);
            session.remove(task);

            tx.commit();
        } catch (Exception exc) {
            if (tx != null)
                tx.rollback();
        }
    }

//    public boolean existsById(int id) {
//        return jdbcTemplate.queryForObject("select exists (select from todo_table where id = ?)", Boolean.class, id);
//    }
    public boolean existsById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(TodoTask.class, id) != null;
        }
    }
}