package authentication.server.controllers;

import java.util.regex.Pattern;

public class Validator {

    private static Pattern pattern;

    public static boolean isValidName(String name) {
        String regex = "^[A-Za-z]\\w{2,29}$";
        pattern = Pattern.compile(regex);
        if(name != null && pattern.matcher(name).matches()) {
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,15}$";
        pattern = Pattern.compile(emailRegex);
        if(email != null && pattern.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = ".*[A-Z].*";
        pattern = Pattern.compile(passwordRegex);
        if(password != null && pattern.matcher(password).matches()) {
            return true;
        }
        return false;
    }

    public static String getPasswordConstraints(){
        return "\ncontains at least 8 characters and at most 20 characters.\n" +
                "At least one digit.\n" +
                "At least one upper case alphabet.\n" +
                "At least one lower case alphabet.\n" +
                "At least one special character which includes !@#$%&*()-+=^.\n" +
                "no white space.";
    }
}
