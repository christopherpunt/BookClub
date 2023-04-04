package bookclub.enums;

public enum NotificationType {
    FriendRequest("friendRequest"),
    BorrowRequest("borrowRequest");

    private final String type;

    NotificationType(String status) {
        this.type = status;
    }

    public String getType() {
        return type;
    }

}
