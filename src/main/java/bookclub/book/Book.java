package bookclub.book;

import bookclub.library.Library;
import com.google.gson.Gson;
import jakarta.persistence.*;

@Entity
public class Book {

    public Book(){

    }

//    public Book(Library libraryId, String title, String author, String isbn, String description, String borrowedFromUser, String lentToUser){
//        this.LibraryId = libraryId;
//        this.Title = title;
//        this.Author = author;
//        this.Isbn = isbn;
//        this.Description = description;
//        this.BorrowedFromUser = borrowedFromUser;
//        this.LentToUser = lentToUser;
//    }

    //region Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;
    @Column
    private String Title;
    @Column
    private String Author;
    @Column
    private String Isbn;
    @Column
    private String Description;
    @Column
    private String BorrowedFromUser;
    @Column
    private String LentToUser;
    //endregion

    //region Accessors
    public int getId(){return Id;}
    public void setId(int id) {Id =id;}

    public Library getLibraryId() {return library;}
    public void setLibraryId(Library id) {this.library = id;}

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