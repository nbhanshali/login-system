package Controller;

import UseCase.UserAccess;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminPage implements Selectable{
    private LoginPage logInController;
    private final UserAccess acctServiceManager;

    public AdminPage(UserAccess acctServiceManager){
        this.acctServiceManager = acctServiceManager;
    }

    public LoginPage getLogInController() {
        return logInController;
    }

    public void setLogInController(LoginPage logInController) {
        this.logInController = logInController;
    }

    public UserAccess getAcctServiceManager() {
        return acctServiceManager;
    }

    /**
     * handle the input command and direct it to relevant operations on users
     * - 1 for delete a user
     * - 2 for ban a user
     * - 3 for unban a user
     * - 4 for make an existing user an admin
     * - 5 for quit Admin mode
     * - 6 for log out
     */
    public void selectAccountOption(String userID){
        boolean selectionIsCompleted = false;
        while (!selectionIsCompleted){
            Scanner inputOption = new Scanner(System.in);
            System.out.println("Please do the following:");
            System.out.println("" +
                    "- press 1 to delete a user\n" +
                    "- press 2 to ban a user\n" +
                    "- press 3 to unban a user\n" +
                    "- press 4 to make an existing user an admin\n" +
                    "- press 5 to quit Admin mode\n" +
                    "- press 6 to log out"
            );
            try{
                int newIn = inputOption.nextInt();
                selectionIsCompleted = executeCommand(userID, newIn);
            }catch (InputMismatchException e){
                System.out.println("Invalid input format, please try again");
            }
        }
    }

    /**
     * helper method: Evaluate the input and directing to relevant operations
     *
     * @param userID the user ID of the admin
     * @param cmd the input command
     * @return which the command is valid
     */
    private boolean executeCommand(String userID, int cmd){
        boolean executionIsCompleted = false;
        switch (cmd){
            case 1:
                delete();
                break;
            case 2:
                ban();
                break;
            case 3:
                unban();
                break;
            case 4:
                makeAdmin();
                break;
            case 5:
                System.out.println("Successfully quit Admin Mode!");
                getLogInController().selectAccountOption(userID);
                executionIsCompleted = true;
                break;
            case 6:
                System.out.println("You are now logged out.");
                executionIsCompleted = true;
                break;
            default:
                System.out.println("Please input a valid command");
        }
        return executionIsCompleted;
    }

    /**
     * delete the user with the input username
     */
    public void delete() {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter the username of the user to delete: ");
        String username = in.nextLine();
        if (getAcctServiceManager().exists(username)) {
            if (getAcctServiceManager().findUser(username).getIsAdmin()) {
                System.out.println("Invalid action. You cannot delete admins.");
            } else if (getAcctServiceManager().delete(username)) {
                System.out.println("Successfully deleted");
            }
        }
        else{
            System.out.println("User does not exist");
        }
    }

    /**
     * ban the user with the input username
     */
    public void ban() {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter the username of the user to ban: ");
        String username = in.nextLine();
        if (getAcctServiceManager().exists(username)) {
            if (getAcctServiceManager().findUser(username).getIsAdmin()) {
                System.out.println("Invalid action. You cannot ban admins.");
            } else if (getAcctServiceManager().ban(username)) {
                System.out.println("Successfully banned");
            }
        }
        else{
            System.out.println("User does not exist");
        }
    }

    /**
     * unban the user with the input username
     */
    public void unban() {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter the username of the user to unban: ");
        String username = in.nextLine();
        if(getAcctServiceManager().unban(username)){
            System.out.println("Successfully unbanned");
        }else{
            System.out.println("User does not exist");
        }
    }

    /**
     * make the user with the input username admin
     */
    public void makeAdmin() {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter the username of the user to grant admin rights: ");
        String username = in.nextLine();
        if(getAcctServiceManager().makeAdmin(username)){
            System.out.println("Admin rights granted");
        }else{
            System.out.println("User does not exist");
        }
    }
}
