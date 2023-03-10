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

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
