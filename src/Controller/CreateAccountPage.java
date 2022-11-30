package Controller;

import UseCase.UserAccess;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A controller class responsible for allowing the user to create new User Accounts.
 *
 * =====Private Attribute=====:
 *  loginController - the LoginPage controller to allow user to conduct user account activities after the account
 *                    creation is completed
 *  acctServiceManager - the UserAccess class to allow the program to execute user's command
 */

public class CreateAccountPage {

    private LoginPage loginPageController;
    private final UserAccess acctServiceManager;

    public CreateAccountPage(UserAccess acctServiceManager){
        this.acctServiceManager = acctServiceManager;
    }

    public UserAccess getAcctServiceManager() {
        return acctServiceManager;
    }

    public LoginPage getLoginPageController() {
        return loginPageController;
    }

    public void setLoginPageController(LoginPage loginPageController) {
        this.loginPageController = loginPageController;
    }

    public void createAccount() {
        boolean creatIsCompleted = false;
        while(!creatIsCompleted){
            try{
                // Use scanner to get user's username input from the command line
                Scanner input = new Scanner(System.in);
                // Take input from user
                System.out.println("Enter your username: ");
                String newUsername = input.nextLine();
                // Check if newUsername is taken
                if (getAcctServiceManager().exists(newUsername)) {
                    if(!createAgain()){
                        creatIsCompleted = true;
                    }
                } else {
                    // Take in password input from user
                    System.out.println("Enter your password: ");
                    String newPassword = input.nextLine();
                    // Create new user & add to UserAccess userInfo
                    getAcctServiceManager().addUser(newUsername, newPassword);
                    // User is automatically logged in after creating an account
                    getLoginPageController().nonBannedUserMove(newUsername, false);
                    creatIsCompleted = true;
                }
                // Close input stream for username
            }catch (InputMismatchException e){
                System.out.println("Invalid input format, please try again.");
            }
        }
    }

    /**
     * A private helper to decide next steps when the input username already exists.
     */

    private boolean createAgain() {
        boolean creat = false;
        boolean finishCheck = false;
        while (!finishCheck){
            // Take in value input from user
            System.out.println("Username is already taken. Enter 1 to try again or enter 2 to log in instead: ");
            try {
                Scanner newVal = new Scanner(System.in);
                int val = newVal.nextInt();
                // Proceed to next steps depending on user's choice (1 or 2)
                switch (val) {
                    case 1:
                        finishCheck = true;
                        creat = true;
                        break;
                    case 2:
                        getLoginPageController().startLogin();
                        finishCheck = true;
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            }
            catch (InputMismatchException e){
                System.out.println("Invalid input format, please try again.");
            }
        }
        return creat;
    }
}
