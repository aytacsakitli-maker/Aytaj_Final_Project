package FinalProject.service;

import FinalProject.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserRepository {

    private static final String FILE_PATH = "users.txt";
    private static final String DELIMITER = ";";

    private final List<User> userList = new ArrayList<>();
    private static final UserRepository INSTANCE = new UserRepository();

    private UserRepository() {
        loadDataFromFile();
    }

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    public synchronized void loadDataFromFile() {
        userList.clear();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            userList.add(new User("admin", "Aytac Sakitli", "1234", "PREMIUM"));
            userList.add(new User("oxucu", "Tələbə İstifadəçi", "1234", "FREE"));
            saveDataToFile();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(DELIMITER);
                if (parts.length >= 4) {
                    userList.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("İstifadəçiləri fayldan oxuyarkən xəta: " + e.getMessage());
        }
    }

    public synchronized void saveDataToFile() {
        File file = new File(FILE_PATH);
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8))) {
            for (User u : userList) {
                String line = u.getUsername() + DELIMITER +
                        u.getFullName() + DELIMITER +
                        u.getPassword() + DELIMITER +
                        u.getSubscriptionType();
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            System.err.println("İstifadəçiləri fayla yazarkən xəta: " + e.getMessage());
        }
    }

    public synchronized boolean addUser(User user) {
        if (findByUsername(user.getUsername()) != null) {
            return false;
        }
        userList.add(user);
        saveDataToFile();
        return true;
    }

    public synchronized boolean updatePassword(String username, String newPassword) {
        User user = findByUsername(username);
        if (user != null) {
            user.setPassword(newPassword.trim());
            saveDataToFile();
            return true;
        }
        return false;
    }

    public synchronized boolean updateSubscription(String username, String newSubscriptionType) {
        User user = findByUsername(username);
        if (user != null) {
            user.setSubscriptionType(newSubscriptionType.trim().toUpperCase());
            saveDataToFile();
            return true;
        }
        return false;
    }

    public synchronized boolean deleteUser(String username) {
        User user = findByUsername(username);
        if (user != null) {
            userList.remove(user);
            saveDataToFile();
            return true;
        }
        return false;
    }

    public synchronized User findByUsername(String username) {
        if (username == null || username.isBlank()) return null;
        for (User u : userList) {
            if (u.getUsername().equalsIgnoreCase(username.trim())) {
                return u;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return Collections.unmodifiableList(new ArrayList<>(userList));
    }
}