package UseCase;

import Entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class UserAccessTest {

    private static UserAccess users;

    @Before
    public void before(){
        users = new UserAccess(new HashMap<>(), new HashMap<>());
        users.addUser("lindsey", "20220612");
    }

    @Test
    public void testUserAccess(){
        HashMap<String, String> info = new HashMap<>();
        info.put("Password", "1234567890");
        info.put("IsBanned", String.valueOf(false));
        info.put("IsAdmin", String.valueOf(true));
        HashMap<String, HashMap<String, String>> userinfo = new HashMap<>();
        userinfo.put("kevin", info);
        ArrayList<Timestamp> loginHistory = new ArrayList<>();
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        loginHistory.add(dateTime);
        HashMap<String, ArrayList<Timestamp>> userLoginHistory = new HashMap<>();
        userLoginHistory.put("kevin", loginHistory);
        UserAccess newUserAccess = new UserAccess(userinfo, userLoginHistory);
        assertTrue(newUserAccess.exists("kevin"));
        assertEquals(newUserAccess.findUser("kevin").getLoginHistory(), loginHistory);
        assertFalse(newUserAccess.exists("lindsey"));

    }

    @Test
    public void testGetUsers(){
        ArrayList<User> currUsers = users.getUsers();
        assertEquals(currUsers.size(), 1);
        User user = users.findUser("lindsey");
        assertTrue(currUsers.contains(user));

    }

    @Test
    public void testSetUsers(){
        ArrayList<User> givenUsers = new ArrayList<>();
        User newUser = new User("kevin", "1234567890");
        givenUsers.add(newUser);
        users.setUsers(givenUsers);
        assertTrue(users.exists("kevin"));
        assertFalse(users.exists("lindsey"));

    }

    @Test
    public void testCheckLogin() {
        HashMap<String, Boolean> res = users.checkLogin("lindsey", "20220612");
        assertTrue(res.containsKey("IsAdmin"));
        assertFalse(res.get("IsAdmin"));
        assertTrue(res.containsKey("IsBanned"));
        assertFalse(res.get("IsBanned"));
        assertTrue(res.containsKey("Exists"));
        assertTrue(res.get("Exists"));
    }

    @Test
    public void testExists() {
        assertTrue(users.exists("lindsey"));
    }

    @Test
    public void testFindUser() {
        User result = users.findUser("lindsey");
        assertEquals("lindsey", result.getUsername());
        assertEquals("20220612", result.getPassword());
        assertFalse(result.getLoginHistory().isEmpty());
        assertFalse(result.getIsBanned());
        assertFalse(result.getIsAdmin());

        assertNull(users.findUser("kevin"));
    }

    @Test
    public void testAddUser() {
        users.addUser("kevin", "1234567890");
        User result = users.findUser("kevin");
        assertEquals("kevin", result.getUsername());
        assertEquals("1234567890", result.getPassword());
    }

    @Test
    public void testDelete() {
        users.addUser("kevin", "1234567890");
        users.delete("kevin");
        User result = users.findUser("kevin");
        assertNull(result);


    }

    @Test
    public void testBan() {
        users.addUser("kevin", "1234567890");
        assertTrue(users.ban("kevin"));
        assertFalse(users.ban("mike"));

    }

    @Test
    public void testUnban() {
        users.addUser("kevin", "1234567890");
        users.ban("kevin");
        assertTrue(users.unban("kevin"));

    }

    @Test
    public void testMakeAdmin() {
        users.addUser("kevin", "1234567890");
        assertTrue(users.makeAdmin("kevin"));
        assertFalse(users.makeAdmin("Mike"));

    }

    @Test
    public void testGetPreviousLogin() {
        ArrayList<String> previousLogin1 = users.getPreviousLogin("lindsey");
        assertTrue(previousLogin1.contains("Hey lindsey, you haven't logged in previously"));
    }

    @Test
    public void testOutputUserInfo() {
        HashMap<String, HashMap<String, String>> outputInfo = users.outputUserInfo();
        assertEquals(outputInfo.get("lindsey").get("Password"), "20220612");
        assertEquals(outputInfo.get("lindsey").get("IsBanned"), String.valueOf(false));
        assertEquals(outputInfo.get("lindsey").get("IsAdmin"), String.valueOf(false));

    }

    @Test
    public void testUserLoginHistory() {
        assertEquals(users.findUser("lindsey").getLoginHistory(), users.userLogInHistory().get("lindsey"));
    }

    @After
    public void after(){
        users.delete("lindsey");
    }

}