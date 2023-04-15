package bookclub.enums;

public enum NotificationData {
    BOOKID("bookId"),
    USER_EMAIL("user_email"),
    CREATED_FRIEND("createdFriend");

    private final String key;

    NotificationData(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
