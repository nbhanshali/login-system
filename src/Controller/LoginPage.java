package Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import UseCase.UserAccess;

/**
 * A controller class responsible for allowing user to conduct LoginPage activities
 *
 * =====Private Attribute=====:
 * adminController - the AdminPage controller to allow user to conduct admin account activities
 * acctServiceManager - the UserAccess class to allow the program to execute user's command
 *
 */

public class LoginPage implements Selectable{

    private AdminPage adminController;
    private final UserAccess acctServiceManager;

    public LoginPage(UserAccess acctServiceManager){
        this.acctServiceManager = acctServiceManager;
    }

    public UserAccess getAcctServiceManager() {
        return acctServiceManager;
    }

    public void setAdminController(AdminPage adminController) {
        this.adminController = adminController;
    }

    public AdminPage getAdminController() {
        return adminController;
    }

    /**
     * The public method to be invoked when a user starts log in
     *
     */
    public void startLogin() {
        boolean tempFlag = true; // a temporary object that stays true while the program is running
        while (tempFlag){
            try{
                //Using Scanner to get Input from User through command line
                Scanner inputStream = new Scanner(System.in);

                // Take input from User
                System.out.println("Please enter username: ");
                String userID = inputStream.nextLine();
                System.out.println("Please enter password: ");
                String pwd = inputStream.nextLine();

                // User Authentication
                HashMap<String, Boolean> authenStatus = getAcctServiceManager().checkLogin(userID, pwd);
                boolean exists = authenStatus.get("Exists");
                boolean isBanned = authenStatus.get("IsBanned");
                boolean isAdmin = authenStatus.get("IsAdmin");

                // Next Move
                nextMove(userID, exists, isBanned, isAdmin);
                tempFlag = false;

            }catch (InputMismatchException e){
                System.out.println("Invalid input format, please try again.");
            }
        }
    }

    /**
     * A private helper to determine the next move that a user can take based on the account authentication status
     * @param userID a String of userID
     * @param isUserExisted a boolean to represent whether the User account exists
     * @param isBanned a boolean to represent whether the User account is banned
     * @param isAdmin a boolean to represent whether the User account is an Admin account
     *
     */
    private void nextMove(String userID, boolean isUserExisted, boolean isBanned,
                             boolean isAdmin) {
        if (isUserExisted){
            if (isBanned){
                System.out.println("This account is banned, you will be directed back to the MainPage");
            }
            else{
                nonBannedUserMove(userID, isAdmin);
            }
        }
        else{
            System.out.println("Incorrect username or password, please try again");
        }
    }

    /**
     * A protected helper to conduct move when user is not banned
     * @param userID a String of userID
     * @param isAdmin a boolean to represent whether user is Admin
     */
    protected void nonBannedUserMove(String userID, boolean isAdmin) {
        String welcomeMsg = "You are now logged in as " + userID + ". Welcome!";
        System.out.println(welcomeMsg);
        if (isAdmin){
            getAdminController().selectAccountOption(userID);
        }
        else{selectAccountOption(userID);}
    }

    /**
     * handles the input command after a user is logged in
     * @param userID a String representing the user's username
     */
    public void selectAccountOption(String userID) {
        boolean selectionIsCompleted = false;
        while (!selectionIsCompleted){
            System.out.println("Please choose your options:");
            System.out.println("" +
                    "- press 1 to view previous login history\n" +
                    "- press 2 to login in Admin mode\n"+
                    "- press 3 to log out"
            );
            try {
                // Use Scanner to take user options
                Scanner newUserOption = new Scanner(System.in);
                int userOption = newUserOption.nextInt();
                switch (userOption) {
                    case 1:
                        System.out.println("Hey " + userID + ", you were logged in previously at: ");
                        ArrayList<String> logInHistory = getAcctServiceManager().getPreviousLogin(userID);
                        for (String history: logInHistory){
                            System.out.println(history);
                        }
                        break;
                    case 2:
                        if (getAcctServiceManager().isAdmin(userID)){
                            System.out.println("You are now logged in as an Admin");
                            getAdminController().selectAccountOption(userID);
                            selectionIsCompleted = true;
                        }
                        else{System.out.println("Sorry, you don't have the Admin right");}
                        break;
                    case 3:
                        System.out.println("You are now logged out.");
                        selectionIsCompleted = true;
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            } catch (InputMismatchException e){
                System.out.println("Invalid input format, please try again.");
            }
        }
    }
}
