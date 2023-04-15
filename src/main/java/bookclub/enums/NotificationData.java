package bookclub.enums;

public enum NotificationData {
    BOOKID("bookId");

    private final String key;

    NotificationData(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
