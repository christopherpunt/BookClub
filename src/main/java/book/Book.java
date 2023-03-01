package book;

import com.google.gson.Gson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Book {

    public Book(){

    }

    public Book(int id, int libraryId, String title, int isbn, String description, String borrowedFromUser, String lentToUser){
        this.Id = id;
        this.LibraryId = libraryId;
        this.Title = title;
        this.Isbn = isbn;
        this.Description = description;
        this.BorrowedFromUser = borrowedFromUser;
        this.LentToUser = lentToUser;
    }

    //region Properties
    @Id private int Id;
    @Column private Integer LibraryId;
    @Column private String Title;


    @Column private String Author;
    @Column private Integer Isbn;
    @Column private String Description;
    @Column private String BorrowedFromUser;
    @Column private String LentToUser;
    //endregion

    //region Accessors
    public int getId(){return Id;}
    public void setId(int id) {Id =id;}

    public int getLibraryId() {return LibraryId;}
    public void setLibraryId(int id) {this.LibraryId = id;}

    public String getTitle() {return Title;}
    public void setTitle(String title) {Title = title;}

    public String getAuthor() {return Author;}
    public void setAuthor(String author) {Author = author;}

    public int getIsbn() {return Isbn;}
    public void setIsbn(int isbn) {this.Isbn = isbn;}

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
