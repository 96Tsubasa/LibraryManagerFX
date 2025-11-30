package application.repository;

import application.database.Database;
import application.logic.User;

import java.util.List;
import java.util.Optional;

public class DatabaseUserRepository implements UserRepository {
    @Override
    public List<User> loadUsers() {
        return Database.loadUsers();
    }

    @Override
    public Optional<User> findById(long userId) {
        User u = Database.loadUsers().stream().filter(user -> user.getUserId() == userId).findFirst().orElse(null);
        return Optional.ofNullable(u);
    }

    @Override
    public long createNewUserId() {
        return Database.createNewUserId();
    }

    @Override
    public void addUser(User user) {
        Database.addUser(user);
    }

    @Override
    public void editUserById(User user) {
        Database.editUserById(user);
    }

    @Override
    public void deleteUserById(long userId) {
        Database.deleteUserById(userId);
    }
}
