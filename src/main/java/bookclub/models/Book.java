package bookclub.models;

import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book extends BaseEntity {

    public Book(){}

    //constructor for Post request from search results page
    public Book(String json) {
        Book book = fromJson(json);

        this.setId(book.getId());
        this.user = book.getUser();
        this.isOwner = book.isOwner();
        this.Title = book.getTitle();
        this.Author = book.getAuthor();
        this.Isbn = book.getIsbn();
        this.Description = book.getDescription();
        this.BorrowedFromUser = book.getBorrowedFromUser();
        this.LentToUser = book.getLentToUser();
        this.BookCoverUrl = book.getBookCoverUrl();
    }

    //region Properties

    @Column
    private boolean isOwner;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String Title;

    @Column
    private String Author;

    @Column
    private String Isbn;

    @Lob
    @Column(columnDefinition="TEXT")
    private String Description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowed_from_user_id")
    private User BorrowedFromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lent_to_user_id")
    private User LentToUser;

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
