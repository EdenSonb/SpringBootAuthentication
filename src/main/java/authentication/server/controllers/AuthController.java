package authentication.server.controllers;

import authentication.server.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    public AuthController() {}

    @RequestMapping(value = "nothing", method = RequestMethod.POST)
    public ResponseEntity<String> nothing(){
        System.out.println("Hiiii");
        return ResponseEntity.ok("edennnnn");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("password") String password) {
        if(!Validator.isValidName(name)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid name");
        }
        if(!Validator.isValidEmail(email)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid email");
        }
        if(!Validator.isValidPassword(password)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password");
        }
        if(!authService.createNewUser(name, email, password)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User was created");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        if(!Validator.isValidEmail(email)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid email");
        }
        if(!Validator.isValidPassword(password)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password");
        }
        String token = authService.validateUserCredentials(email,password);
        if(token == ""){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User was not found");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(token);
    }
}
