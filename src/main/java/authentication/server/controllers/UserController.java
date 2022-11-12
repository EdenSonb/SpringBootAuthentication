package authentication.server.controllers;

import authentication.server.services.AuthService;
import authentication.server.services.Fields;
import authentication.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    public UserController() { }

    @RequestMapping(value = "/updateName", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateName(@RequestParam String token, @RequestParam String name) {
        int id = this.isValidToken(token);
        Validator.isValidName(name);
        if(!userService.updateUserDetails(id, Fields.NAME, name)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unable to update name. Please try again");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated name");
    }

    @RequestMapping(value = "/updateEmail", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateEmail(@RequestParam String token, @RequestParam String email) {
        int id = isValidToken(token);
        if(id == -1){
            return invalidToken();
        }
        if(!Validator.isValidEmail(email)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid email");
        }
        if(!authService.emailIsFree(email)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email is occupied. Please choose a different one");
        }
        if(!userService.updateUserDetails(id, Fields.EMAIL, email)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update email. Please try again");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated email");
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.PATCH)
    public ResponseEntity<String> updatePassword(@RequestParam String token, @RequestParam String password) {
        int id = isValidToken(token);
        if(id == -1){
            return invalidToken();
        }
        if(!Validator.isValidPassword(password)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password");
        }
        userService.updateUserDetails(id, Fields.PASSWORD, password);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated password");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestParam String token) {
        int id = isValidToken(token);
        if(id == -1){
            return invalidToken();
        }
        if(!userService.delete(id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unable to delete user");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body("User was deleted");
    }

    public int isValidToken(@RequestParam String token) {
        return authService.isValidToken(token);
    }

    private ResponseEntity<String> invalidToken(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");
    }
}
