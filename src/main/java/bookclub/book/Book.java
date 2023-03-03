package bookclub.book;

import com.google.gson.Gson;
import jakarta.persistence.*;

@Entity
public class Book {

    public Book(){

    }

    public Book(String libraryId, String title, String author, String isbn, String description, String borrowedFromUser, String lentToUser){
        this.LibraryId = libraryId;
        this.Title = title;
        this.Author = author;
        this.Isbn = isbn;
        this.Description = description;
        this.BorrowedFromUser = borrowedFromUser;
        this.LentToUser = lentToUser;
    }

    //region Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "libraryid")
    private String LibraryId;
    @Column
    private String Title;
    @Column
    private String Author;
    @Column
    private String Isbn;
    @Column
    private String Description;
    @Column(name = "borrowedfromuser")
    private String BorrowedFromUser;
    @Column(name = "lenttouser")
    private String LentToUser;
    //endregion

    //region Accessors
    public int getId(){return Id;}
    public void setId(int id) {Id =id;}

    public String getLibraryId() {return LibraryId;}
    public void setLibraryId(String id) {this.LibraryId = id;}

    public String getTitle() {return Title;}
    public void setTitle(String title) {Title = title;}

    public String getAuthor() {return Author;}
    public void setAuthor(String author) {Author = author;}

    public String getIsbn() {return Isbn;}
    public void setIsbn(String isbn) {this.Isbn = isbn;}

    public String getDescription() {return Description;}
    public void setDescription(String description) {this.Description = description;}

    public String getBorrowedFromUser() {return BorrowedFromUser;}
    public void setBorrowedFromUser(String borrowedFromUser) {this.BorrowedFromUser = borrowedFromUser;}

    public String getLentToUser() {return LentToUser;}
    public void setLentToUser(String lentToUser) {this.LentToUser = lentToUser;}
    //endregion

    public String sayHello(){
        return "Hello World!";
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
