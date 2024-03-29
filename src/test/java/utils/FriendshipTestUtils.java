package utils;

import bookclub.friend.Friendship;
import bookclub.user.User;

import java.util.ArrayList;
import java.util.List;

public class FriendshipTestUtils {
    public static List<Friendship> createFriendships(User user, User ... friends){
        List<Friendship> friendships = new ArrayList<>();
        for (User friend : friends) {
            friendships.add(new Friendship(user, friend));
        }

        return friendships;
    }
}
