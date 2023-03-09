package bookclub.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class User {

    //region Properties
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String Id;
    @Column
    public String firstName;
    @Column
    public String lastName;
    @Column
    public String userName;
    //endregion

    //region Accessors
    public String getId() {return Id;}

    public void setId(String id) {Id = id;}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}
    //endregion
}
