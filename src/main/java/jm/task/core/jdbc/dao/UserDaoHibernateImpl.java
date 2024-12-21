package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import org.hibernate.Session;

import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {


    public UserDaoHibernateImpl() {

    }

    private void executeInTransaction(TransactionalOperation operation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            operation.execute(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUsersTable() {
        executeInTransaction(session -> {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "last_name VARCHAR(255), " +
                    "age SMALLINT" +
                    ")";

            session.createNativeQuery(sql).executeUpdate();
            System.out.println("Таблица users успешно создана");
        });
    }

    @Override
    public void dropUsersTable() {
        executeInTransaction(session -> {
            session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
            System.out.println("Таблица users успешно удалена.");
        });
    }

    @Override
    public void saveUser (String name, String lastName, byte age) {
        executeInTransaction(session -> {
            User user = new User();
            user.setName(name);
            user.setLastName(lastName);
            user.setAge(age);
            session.save(user);
            System.out.println("Пользователь успешно сохранен: " + user);
        });
    }

    @Override
    public void removeUserById(long id) {
        executeInTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                System.out.println("Пользователь с ID " + id + " успешно удален.");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }
        });
    }


@Override
public List<User> getAllUsers() {
    Transaction transaction = null;
    List<User> users;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        transaction = session.beginTransaction();


        Query<User> query = session.createQuery("FROM User", User.class);
        users = query.getResultList();

        transaction.commit();
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        throw new RuntimeException(e);
    }
    return users;
}

    @Override
    public void cleanUsersTable() {
        executeInTransaction(session -> {
            session.createNativeQuery("DELETE FROM users").executeUpdate();
            System.out.println("Таблица users успешно очищена.");
        });
    }

@FunctionalInterface
interface TransactionalOperation {
    void execute(Session session);
}

}
