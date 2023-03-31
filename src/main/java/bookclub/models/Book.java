package bookclub.models;

import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book {

    public Book(){}

    //constructor for Post request from search results page
    public Book(String json) {
        Book book = fromJson(json);

        this.Id = book.getId();
        this.User = book.getUser();
        this.Title = book.getTitle();
        this.Author = book.getAuthor();
        this.Isbn = book.getIsbn();
        this.Description = book.getDescription();
        this.BorrowedFromUser = book.getBorrowedFromUser();
        this.LentToUser = book.getLentToUser();
        this.BookCoverUrl = book.getBookCoverUrl();
    }

    //region Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User User;
    @Column
    private String Title;
    @Column
    private String Author;
    @Column
    private String Isbn;

    @Lob
    @Column(columnDefinition="TEXT")
    private String Description;
    @Column
    private String BorrowedFromUser;
    @Column
    private String LentToUser;
    @Column
    private String BookCoverUrl;
    //endregion

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

    public static Book fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Book.class);
    }
}
