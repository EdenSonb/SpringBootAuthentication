package authentication.server.services;

import authentication.server.User.User;
import authentication.server.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    public UserService() {}

    public boolean updateUserDetails(int id, Fields field, String change) {
        Optional<User> user = usersRepository.getUserById(id);

        if(!user.isPresent()){
            return false;
        }

        switch(field) {
            case NAME:
                user.get().setName(change);
                break;
            case EMAIL:
                user.get().setEmail(change);
                break;
            case PASSWORD:
                user.get().setPassword(change);
                break;
        }
        return usersRepository.updateUserDetails(user.get());
    }

    public boolean delete(int id) {
        return usersRepository.deleteUser(id);
    }
}
