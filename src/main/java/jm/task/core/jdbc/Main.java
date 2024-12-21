package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;


public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("John", "Doe", (byte) 25);
        userService.saveUser("Jane", "Smith", (byte) 30);
        userService.saveUser("Alice", "Johnson",(byte) 28);
        userService.saveUser("Bob", "Brown",(byte) 22);

        userService.getAllUsers()
                .forEach(System.out::println);

        userService.cleanUsersTable();
        userService.dropUsersTable();

        userService.close();
    }
}
