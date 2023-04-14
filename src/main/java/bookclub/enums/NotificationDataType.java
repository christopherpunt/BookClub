package bookclub.enums;

public enum NotificationDataType {
    BOOKID;

    private final Long bookId;

    NotificationDataType(Long bookId) {
        this.bookId = bookId;
    }

    public Long getBookId() {
        return bookId;
    }
}
