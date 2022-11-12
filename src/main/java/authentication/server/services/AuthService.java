package authentication.server.services;

import authentication.server.User.User;
import authentication.server.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
@Service
public class AuthService {
    @Autowired
    private UsersRepository usersRepository;

    private final Map<String, Integer> tokenId;

    public AuthService() {
        tokenId = new HashMap<>();
    }

    public boolean createNewUser(String name, String email, String password){
        if(usersRepository.emailIsFree(email) == false){
            return false;
        }
        User newUser = new User(createId(), name, email, password);
        usersRepository.writeUserToRepo(newUser);
        return true;
    }

    public String validateUserCredentials(String email, String password){
        int id = usersRepository.userIsValid(email, password);
        if(id == -1){
            return "";
        }
        String token = createToken();
        tokenId.put(token, id);
        return token;
    }

    public int isValidToken(String token){
        if(tokenId.containsKey(token)) {
            return tokenId.get(token);
        }
        return -1;
    }

    private String createToken(){
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder;
        do {
            stringBuilder = new StringBuilder(8);
            for (int i = 0; i < 6; i++) {
                stringBuilder.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));
            }
        }
        while (tokenId.get(stringBuilder) != null);
        return stringBuilder.toString();
    }

    private int createId(){
        int newId;
        do {
            newId = ThreadLocalRandom.current().nextInt(999);
        }
        while(!usersRepository.idIsFree(newId));
        return newId;
    }

    public boolean emailIsFree(String email){
        return usersRepository.emailIsFree(email);
    }
}
