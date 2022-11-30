package Controller;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A controller class to provide MainMenu functionality for user
 *
 * =====Private Attribute=====:
 * loginController - the LoginPage controller to allow user to login
 * acctCreationController - the CreateAccountPage controller to allow user to creat account
 *
 */
public class MainMenu {

    private final LoginPage loginController;
    private final CreateAccountPage acctCreationController;

    public MainMenu(LoginPage loginController, CreateAccountPage acctCreationController){
        this.loginController = loginController;
        this.acctCreationController = acctCreationController;
    }

    public LoginPage getLoginController() {
        return loginController;
    }

    public CreateAccountPage getAcctCreationController() {
        return acctCreationController;
    }

    public void input(){
        // Conduct a while loop to provide system service until the user decide to log out
        boolean exitProgram = false;
        while(!exitProgram){
            System.out.println("Welcome to our login system!");
            System.out.println("To create an account, please enter 1");
            System.out.println("To login, please enter 2");
            System.out.println("To exit the program, please enter 3");
            try{
                Scanner newUserInput = new Scanner(System.in);
                int newOption = newUserInput.nextInt();
                switch (newOption) {
                    case 1:
                        getAcctCreationController().createAccount();
                        break;
                    case 2:
                        getLoginController().startLogin();
                        break;
                    case 3:
                        exitProgram = true;
                        System.out.println("Thanks for choosing group_0244!, Goodbye!");
                        break;
                    default:
                        System.out.println("Your input is invalid!");
                        System.out.println("Please enter a valid input");
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid input format, please try again");
            }
        }
    }
}