package UseCase;

import Entity.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * A class responsible for storing all users and with adding information to/extracting information from users.
 */
public class UserAccess {
    private ArrayList<User> users = new ArrayList<>();

    /**
     * Constructor for a UserAccess object
     * @param userinfo the user info HashMap with the following format
     *                 {"userName":{"Password": String, "IsBanned": String, "IsAdmin": String}}
     * @param userLogInHistory the user log-in history HashMap with the following format
     *                 {"userName": [Timestamp1, Timestamp2, Timestamp3,...]}
     */
    public UserAccess(HashMap<String, HashMap<String, String>> userinfo,
                      HashMap<String, ArrayList<Timestamp>> userLogInHistory){
        for (String userName: userinfo.keySet()){
            HashMap<String, String> currUserInfoDetail = userinfo.get(userName);
            String pwd = currUserInfoDetail.get("Password");
            boolean isBanned = Boolean.parseBoolean(currUserInfoDetail.get("IsBanned"));
            boolean isAdmin = Boolean.parseBoolean(currUserInfoDetail.get("IsAdmin"));
            ArrayList<Timestamp> loginHistory = userLogInHistory.get(userName);
            User currUser = new User(userName, pwd, loginHistory, isBanned, isAdmin);
            users.add(currUser);
        }
    }

    /**
     * getter for the users list
     * @return the users list
     */
    protected ArrayList<User> getUsers(){
        return users;
    }

    /**
     * setter for users attribute
     * @param users the new users list
     */
    protected void setUsers(ArrayList<User> users){
        this.users = users;
    }

    /**
     * A method to check if a user exists with the username and password pair specified as well as other
     * information relevant to logging in.
     *
     * @param username a username
     * @param password a password
     * @return a hashmap representing important information such as if this user exists,
     * if they are banned, and if they are admin.
     */
    public HashMap<String, Boolean> checkLogin(String username, String password){
        HashMap<String, Boolean> results = new HashMap<>();
        //Set each property to false by default
        results.put("Exists", false);
        results.put("IsBanned", false);
        results.put("IsAdmin", false);
        //Loop to check if a user exists with this username password pair
        for (User user: users){
            //update results if such a user exists and update their loginHistory
            if ((Objects.equals(user.getUsername(), username)) && (Objects.equals(user.getPassword(), password))){
                results.replace("Exists", true);

                //update the login history
                updateLoginHistory(user);

                //update results if this user is banned
                if (user.getIsBanned()){
                    results.replace("IsBanned", true);
                }
                //update results if this user is an admin
                if (user.getIsAdmin()){
                    results.replace("IsAdmin", true);
                }
            }
        }
        return results;
    }

    /**
     * A private helper to update the user log-in info
     * @param user: the user entity object
     */
    private void updateLoginHistory(User user){
        //Date object
        Date date= new Date();
        //getTime() returns current time in milliseconds
        long time = date.getTime();
        //Passed the milliseconds to constructor of Timestamp class
        Timestamp ts = new Timestamp(time);
        user.getLoginHistory().add(ts);
    }

    /**
     * Checks if a user with this username exists.
     *
     * @param username a username
     * @return true if this user exists, false otherwise.
     */
    public boolean exists(String username){
        for (User user: users){
            //update results if such a user exists and update their loginHistory
            if (Objects.equals(user.getUsername(), username)){
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a user with the specified username.
     *
     * @param username a username
     * @return if a User with username exists, return the User object, otherwise return null
     */
    public User findUser(String username){
        for (User user: users){
            if (Objects.equals(user.getUsername(), username)){
                return user;
            }
        }
        return null;
    }

    /**
     * Creates a new user with username and password and adds the user to the users list.
     *
     * @param username a username
     * @param password a password
     */
    public void addUser(String username, String password){
        User currUser = new User(username, password);
        updateLoginHistory(currUser);
        users.add(currUser);
    }

    /**
     * Removes the User with username from users.
     *
     * @param username a username
     * @return true if a User with username existed and the deletion was successful, false otherwise.
     */
    public boolean delete(String username){
        if (this.exists(username)){
            users.remove(this.findUser(username));
            return true;
        }
        return false;
    }

    /**
     * Bans the User with username.
     *
     * @param username a username
     * @return true if a User with username existed and the banning was successful, false otherwise.
     */
    public boolean ban(String username){
        //check if User with username exists
        if (this.exists(username)){
            User foundUser = this.findUser(username);
            //Since we now know that a User with username exists, foundUser must be type User and not null
            assert foundUser != null;
            foundUser.setBanned(true);
            return true;
        }
        return false;
    }

    /**
     * Unbans the User with username.
     *
     * @param username a username
     * @return true if a User with username existed and the unbanning was successful, false otherwise.
     */
    public boolean unban(String username){
        //check if User with username exists
        if (this.exists(username)){
            User foundUser = this.findUser(username);
            //Since we now know that a User with username exists, foundUser must be type User and not null
            assert foundUser != null;
            foundUser.setBanned(false);
            return true;
        }
        return false;
    }

    /**
     * Makes the User with username an admin
     * @param username the username of a User
     * @return true if a User with username existed and was made admin, false otherwise.
     */
    public boolean makeAdmin(String username){
        if (this.exists(username)){
            User foundUser = this.findUser(username);
            //Since we now know that a User with username exists, foundUser must be type User and not null
            assert foundUser != null;
            foundUser.setIsAdmin(true);
            return true;
        }
        return false;
    }

    /**
     * Determine whether the User with username is an admin
     * @param username the username of a User
     * @return true if a User with username is an admin, false otherwise.
     *
     * Precondition:
     * the User with <username> exists in this.users.
     *
     */
    public boolean isAdmin(String username){
        return findUser(username).getIsAdmin();
    }

    /**
     *
     * @param username the username of a User
     * @return a String representation for the previous login Timestamp of the User with <username>
     *
     * Precondition:
     * The User with <username> exists in this.users.
     */
    public ArrayList<String> getPreviousLogin(String username){
        User currUser = findUser(username);
        ArrayList<Timestamp> loginHistory = currUser.getLoginHistory();
        ArrayList<String> stringOfLoginHistory = new ArrayList<>();
        for (Timestamp history: loginHistory){
            String timestamp = history.toString();
            stringOfLoginHistory.add(timestamp);
        }
        return stringOfLoginHistory;
    }

    /**
     *
     * @return the user info HashMap with the following format
     *        {"userName":{"Password": String, "IsBanned": String, "IsAdmin": String}}
     */
    public HashMap<String, HashMap<String, String>> outputUserInfo(){
        HashMap<String, HashMap<String, String>> userInfo = new HashMap<>();
        for (User user: getUsers()){
            HashMap<String, String> userInfoDetail = new HashMap<>();
            String userName = user.getUsername();
            String password = user.getPassword();
            String isBanned = Boolean.toString(user.getIsBanned());
            String isAdmin = Boolean.toString(user.getIsAdmin());
            userInfoDetail.put("Password", password);
            userInfoDetail.put("IsBanned", isBanned);
            userInfoDetail.put("IsAdmin", isAdmin);
            userInfo.put(userName, userInfoDetail);
        }
        return userInfo;
    }

    /**
     *
     * @return the user login history with the following format
     *         {"userName": [Timestamp1, Timestamp2, Timestamp3,...]}
     */
    public HashMap<String, ArrayList<Timestamp>> userLogInHistory(){
        HashMap<String, ArrayList<Timestamp>> loginHistory = new HashMap<>();
        for (User user: users){
            loginHistory.put(user.getUsername(), user.getLoginHistory());
        }
        return loginHistory;
    }
}
