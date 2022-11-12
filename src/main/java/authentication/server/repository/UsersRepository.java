package authentication.server.repository;

import authentication.server.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UsersRepository {
    private final Map<Integer, User> userMap;

    public UsersRepository() {
        userMap = new HashMap<>();
        this.loadMap();
    }

    private void loadMap() {
        String userDirectory = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(userDirectory), "*.json")) {
            Map<String, String> user;
            for (Path p : stream) {
                user = ReadWriteToJson.readFromJson(p.toString());
                if (user != null && !user.isEmpty() && user.containsKey("id") && user.containsKey("name") && user.containsKey("email") && user.containsKey("password")) {
                    User newUser = new User(Integer.parseInt(user.get("id")), user.get("name"), user.get("email"), user.get("password"));
                    this.userMap.put(Integer.parseInt(user.get("id")), newUser);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void writeUserToRepo(User newUser) {
        String userDirectory = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        String fileName = userDirectory + "/" + String.valueOf(newUser.getId()) + ".json";
        Map<String, String> user = new HashMap<>();
        user.put("id", String.valueOf(newUser.getId()));
        user.put("name", newUser.getName());
        user.put("email", newUser.getEmail());
        user.put("password", newUser.getPassword());
        if(!ReadWriteToJson.writeToJson(fileName, user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        this.userMap.put(Integer.parseInt(user.get("id")), new User(Integer.parseInt(user.get("id")), user.get("name"), user.get("email"), user.get("password")));
    }

    public Optional<User> readUserFromRepo(String userEmail) {
        if (this.userMap != null && !this.userMap.isEmpty()) {
            for (User user : this.userMap.values()) {
                if (user.getEmail().equals(userEmail)) {
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<User> getUserById(int id) {
        if (this.userMap != null && !this.userMap.isEmpty()) {
            return Optional.of(this.userMap.get(id));
        }
        return Optional.empty();
    }

    public int userIsValid(String email, String password) {
        for (User user : userMap.values()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user.getId();
            }
        }
        return -1;
    }

    public boolean emailIsFree(String email) {
        for (User user : userMap.values()) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    public boolean idIsFree(int id) {
        if ((this.userMap != null && !this.userMap.isEmpty() && userMap.get(id) == null) || this.userMap.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean deleteUser(int id) {
        String userDirectory = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        File file = new File(userDirectory + "/" + id + ".json");
        if (file.delete()) {
            //System.out.println("Deleted the file: " + file.getName());
            userMap.remove(id);
            return true;
        }
            //System.out.println("Can't delete file. File " + file.getName() + " doesn't exist.");
        return false;
    }

    public boolean updateUserDetails(User user) {
        if (this.userMap != null && !this.userMap.isEmpty()) {
            User editedUser = userMap.get(user.getId());
            editedUser.setName(user.getName());
            editedUser.setEmail(user.getEmail());
            editedUser.setPassword(user.getPassword());
            writeUserToRepo(editedUser);
            return true;
        }
        return false;
    }
}
