package utils;

import bookclub.models.User;

import java.util.Random;

public class UserTestUtils {

    public static User
    createUser(String name){
        User user = new User();
        user.setId(new Random().nextLong());
        user.setFirstName(name.split(" ")[0]);
        user.setLastName(name.split(" ")[1]);
        user.setEmail(user.getFirstName() + "@email.com");
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
