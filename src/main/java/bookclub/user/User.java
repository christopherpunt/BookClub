package bookclub.user;

import bookclub.library.Library;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

    //region Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;
    @Column
    public String firstName;
    @Column
    public String lastName;
    @Column
    public String userName;

    @OneToMany(mappedBy = "User")
    public List<Library> Libraries;
    //endregion

    //region Accessors
    public int getId() {return Id;}

    public void setId(int id) {Id = id;}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}
    //endregion
}
