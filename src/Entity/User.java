package Entity;

import java.sql.Timestamp;
import java.util.*;

public class User {

    private String username;
    private String password;
    private ArrayList<Timestamp> loginHistory;
    private boolean isBanned;
    private boolean isAdmin;

    /**
     * Regular constructor used in methods of UserAccess
     * @param username the username of this User
     * @param password the password of this User
     */
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.loginHistory = new ArrayList<>();
        this.isBanned = false;
        this.isAdmin = false;
    }

    public User(String username, String password, ArrayList<Timestamp> loginHistory, boolean isBanned, boolean isAdmin){
        this.username = username;
        this.password = password;
        this.loginHistory = loginHistory;
        this.isBanned = isBanned;
        this.isAdmin = isAdmin;
    }

    // username getter and setter
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    //password getter and setter
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    // loginHistory getters and setters
    public ArrayList<Timestamp> getLoginHistory(){
        return loginHistory;
    }
    public void setLoginHistory(ArrayList<Timestamp> loginHistory){
        this.loginHistory = loginHistory;
    }
    // banned getters and setters
    public boolean getIsBanned(){
        return isBanned;
    }
    public void setBanned(boolean isBanned){
        this.isBanned = isBanned;
    }

    public boolean getIsAdmin(){
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    // methods

    public void addLogin(Timestamp dateTime){
        loginHistory.add(dateTime);
    }
}
