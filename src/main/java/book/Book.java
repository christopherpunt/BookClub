package book;

public class Book {

    //region Properties
    private String Id;
    private String libraryId;
    private String Title;
    private int isbn;
    private String description;
    private String borrowedFromUser;
    private String lentToUser;
    //endregion

    //region Accessors
    public String getTitle() {return Title;}

    public void setTitle(String title) {Title = title;}

    public int getIsbn() {return isbn;}

    public void setIsbn(int isbn) {this.isbn = isbn;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public String getBorrowedFromUser() {return borrowedFromUser;}

    public void setBorrowedFromUser(String borrowedFromUser) {this.borrowedFromUser = borrowedFromUser;}

    public String getLentToUser() {return lentToUser;}

    public void setLentToUser(String lentToUser) {this.lentToUser = lentToUser;}
    //endregion

    public String sayHello(){
        return "Hello World!";
    }
}
