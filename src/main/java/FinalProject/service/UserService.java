package FinalProject.service;

import FinalProject.User;

public class UserService {

    private static final UserService INSTANCE = new UserService();
    private final UserRepository repository = UserRepository.getInstance();

    public UserService() {}

    public static UserService getInstance() {
        return INSTANCE;
    }

    public User login(String username, String password) {
        if (username == null || password == null) return null;

        User user = repository.findByUsername(username);
        if (user != null && user.getPassword().equals(password.trim())) {
            return user;
        }
        return null;
    }

    public boolean register(String username, String fullName, String password, String subscriptionType) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }
        User newUser = new User(username.trim(), fullName.trim(), password.trim(), subscriptionType);
        return repository.addUser(newUser);
    }

    public boolean userExists(String username) {
        return repository.findByUsername(username) != null;
    }

    public boolean updatePassword(String username, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) return false;
        return repository.updatePassword(username, newPassword);
    }
}