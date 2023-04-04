package utils;

import bookclub.models.User;

public class UserUtils {

    public static User createUser(String name, String email){
        User user = new User();
        user.setFirstName(name.split(" ")[0]);
        user.setLastName(name.split(" ")[1]);
        user.setEmail(email);
        return user;
    }

    public static User createUser(String firstName, String lastName, String email, String password){
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
