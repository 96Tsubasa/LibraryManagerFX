package test.fakes;

import application.logic.User;
import application.repository.UserRepository;

import java.util.*;

public class FakeUserRepository implements UserRepository {
    private final Map<Long, User> store = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public List<User> loadUsers() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(store.get(userId));
    }

    @Override
    public long createNewUserId() {
        return nextId++;
    }

    @Override
    public void addUser(User user) {
        store.put(user.getUserId(), user);
    }

    @Override
    public void editUserById(User user) {
        store.put(user.getUserId(), user);
    }

    @Override
    public void deleteUserById(long userId) {
        store.remove(userId);
    }
}
