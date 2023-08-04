package bookclub.shared;

public enum StatusEnum {
    CREATED(0),
    UNREAD(1),
    READ(2),
    COMPLETED(90),
    DELETED(99);

    private final int value;

    StatusEnum(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
