package bookclub.models;

import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book {

    public Book(){

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
}
