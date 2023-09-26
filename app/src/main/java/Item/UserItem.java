package Item;

public class UserItem {
    private String userEmail;

    private String userPassword;

    private String userName;
    private String point;

    public UserItem(String userName, String point) {
        this.userName = userName;
        this.point = point;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getPoint() {
        return point;
    }
}
