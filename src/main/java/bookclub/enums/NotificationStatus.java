package bookclub.enums;

public enum NotificationStatus {
    UNREAD("unread"),
    READ("read"),
    COMPLETED("completed");

    private final String status;

    NotificationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
