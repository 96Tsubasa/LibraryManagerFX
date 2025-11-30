package application.repository;

import application.logic.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> loadUsers();

    Optional<User> findById(long userId);

    long createNewUserId();

    void addUser(User user);

    void editUserById(User user);

    void deleteUserById(long userId);
}
