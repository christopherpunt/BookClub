package utils;

import bookclub.models.Friendship;
import bookclub.models.User;

public class FriendshipTestUtils {
    public static Friendship createFriendship(User user, User friend){
        return new Friendship(user, friend);
    }

}
