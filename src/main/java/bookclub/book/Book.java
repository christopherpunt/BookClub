package bookclub.book;

import bookclub.purge.PurgeableEntity;
import bookclub.shared.BaseEntity;
import bookclub.user.User;
import com.google.gson.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

@Getter
@Setter
@Entity
@PurgeableEntity(repositoryClass = BookRepository.class)
public class Book extends BaseEntity {

    public Book(){}

    //constructor for Post request from search results page
    public Book(String json) {
        Book book = fromJson(json);

        this.Title = book.getTitle();
        this.Author = book.getAuthor();
        this.Isbn = book.getIsbn();
        this.BookCoverUrl = book.getBookCoverUrl();
    }

    public Book(String Title, String Author, String Isbn, String bookCoverUrl){
        this.Title = Title;
        this.Author = Author;
        this.Isbn = Isbn;
        this.BookCoverUrl = bookCoverUrl;
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
        JsonObject object = new JsonObject();

        object.addProperty("Title", this.Title);
        object.addProperty("Author", this.Author);
        object.addProperty("Isbn", this.Isbn);
        object.addProperty("BookCoverUrl", this.BookCoverUrl);

        return new Gson().toJson(object);
    }

    public static Book fromJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Book.class, new BookDeserializer())
                .create();
        return gson.fromJson(json, Book.class);
    }

    private static class BookDeserializer implements JsonDeserializer<Book> {
        @Override
        public Book deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String title = jsonObject.get("Title").getAsString();
            String author = jsonObject.get("Author").getAsString();
            String isbn = jsonObject.get("Isbn").getAsString();
            String bookCoverUrl = jsonObject.get("BookCoverUrl").getAsString();

            return new Book(title, author, isbn, bookCoverUrl);
        }
    }
}
