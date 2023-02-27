package user;

public class User {

    //region Properties
    public String Id;
    public String firstName;
    public String lastName;
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
