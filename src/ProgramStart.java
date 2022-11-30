import Controller.AdminPage;
import Controller.CreateAccountPage;
import Controller.LoginPage;
import Controller.MainMenu;
import UseCase.UserAccess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A class with a main method where we would start the program.
 */
public class ProgramStart {
    public static void main(String[] args) throws IOException {
        /* Read UserInfo.csv and UserLoginHistory.csv files to get info for each user
         * Create userAcctInfo HashMap and userLoginHistory HashMap with format describe in the UserAccess constructor
         * This is a temporary implementation for Phase 0 with some hardcoding in it. The entire I/O functionality will
         * be taken care of by the Gateway class in Phase 1 and Phase 2.
        */
        // Populate the userAcctInfo HashMap
        HashMap<String, HashMap<String, String>> userAcctInfo = new HashMap<>();
        String csvFilePath = System.getProperty("user.dir") + "//phase0//src//";
        BufferedReader br = new BufferedReader(new FileReader(csvFilePath+"UserAccountInfo.csv"));
        br.readLine();
        String acctInfoLine;
        while((acctInfoLine = br.readLine()) != null){
            String [] currAcctInfo = acctInfoLine.split(",");
            String userName = currAcctInfo[0];
            String pwd = currAcctInfo[1];
            String isBanned = currAcctInfo[2];
            String isAdmin = currAcctInfo[3];
            HashMap<String, String> infoDetail = new HashMap<>();
            infoDetail.put("Password", pwd);
            infoDetail.put("IsBanned", isBanned);
            infoDetail.put("IsAdmin", isAdmin);
            userAcctInfo.put(userName, infoDetail);
        }
        // close the buffer reader for user account info
        br.close();

        // Initialize the key of the userLogInHistory
        HashMap<String, ArrayList<Timestamp>> userLogInHistory = new HashMap<>();
        for (String userName: userAcctInfo.keySet()){
            userLogInHistory.put(userName, new ArrayList<>());
        }

        /*
         * Populate the log in history. In a relational database environment, the "UserName" column in
         * "UserAcctInfo" table is a <FOREIGN KEY> in the "UserLoginHistory" table.
         */
        BufferedReader br2 = new BufferedReader(new FileReader(csvFilePath+"UserLoginHistory.csv"));
        br2.readLine();
        String logInHistoryLine;
        while((logInHistoryLine = br2.readLine()) != null){
            String [] currHistoryLine = logInHistoryLine.split(",");
            String currUserName = currHistoryLine[0];
            Timestamp currTimeStamp = Timestamp.valueOf(currHistoryLine[1]);
            userLogInHistory.get(currUserName).add(currTimeStamp);
        }
        // close the buffer reader for user log-in history
        br2.close();

        // initialize the one and the only UserAccess object that will be used by all controllers in the Log-in system
        UserAccess userAccessManager = new UserAccess(userAcctInfo, userLogInHistory);


        // initialize all controllers for only once
        LoginPage loginController = new LoginPage(userAccessManager);
        CreateAccountPage createAccountController = new CreateAccountPage(userAccessManager);
        AdminPage adminPageController = new AdminPage(userAccessManager);
        loginController.setAdminController(adminPageController);
        adminPageController.setLogInController(loginController);
        createAccountController.setLoginPageController(loginController);

        // Main program starts
        MainMenu mainPage = new MainMenu(loginController, createAccountController);
        mainPage.input();

        /*
         * Reverse Engineering to output all updated user info and login history to UserInfo.csv and
         * UserLoginHistory.csv files. Not the optimal way to do it, but it should satisfy requirements in Phase 0
         * for now.
         * Will optimize the functionality of output object information from system in Phase 1 and Phase 2
         *
         */
        String updatedUserAcctfile = csvFilePath + "UserAccountInfo.csv";
        String updatedHistory = csvFilePath + "UserLoginHistory.csv";
        HashMap<String, HashMap<String, String>> updatedUserAcctInfo = userAccessManager.outputUserInfo();
        HashMap<String, ArrayList<Timestamp>> updatedUserLogInHistory = userAccessManager.userLogInHistory();
        FileWriter userInfoWriter = new FileWriter(updatedUserAcctfile);
        userInfoWriter.append("UserName,Password,IsBanned,IsAdmin\n");

        ArrayList<String> userInfoKeys = new ArrayList<>(updatedUserAcctInfo.keySet());
        Collections.reverse(userInfoKeys);
        for (String username: userInfoKeys){
            ArrayList<String> infoData = new ArrayList<>();
            infoData.add(username);
            infoData.add(updatedUserAcctInfo.get(username).get("Password"));
            infoData.add(updatedUserAcctInfo.get(username).get("IsBanned"));
            infoData.add(updatedUserAcctInfo.get(username).get("IsAdmin"));
            userInfoWriter.append(String.join(",", infoData));
            userInfoWriter.append("\n");
        }
        userInfoWriter.flush();
        userInfoWriter.close();

        FileWriter logInHistoryWriter = new FileWriter(updatedHistory);
        logInHistoryWriter.append("UserName,LogInHistory\n");

        ArrayList<String> logInHistoryKeys = new ArrayList<>(updatedUserLogInHistory.keySet());
        Collections.reverse(logInHistoryKeys);
        for (String username: logInHistoryKeys){
            for (Timestamp history: updatedUserLogInHistory.get(username)){
                String timeStamp = history.toString();
                logInHistoryWriter.append(String.join(",", username, timeStamp));
                logInHistoryWriter.append("\n");
            }
        }
        logInHistoryWriter.flush();
        logInHistoryWriter.close();
    }
}
