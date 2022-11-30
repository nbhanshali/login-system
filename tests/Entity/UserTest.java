package Entity;

import org.junit.Test;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class UserTest {


    // the User constructors
    @Test(timeout = 50)
    public void testUser1(){
        User user = new User("lindsey", "20220612");
        assertEquals("lindsey", user.getUsername());
        assertEquals("20220612", user.getPassword());
        assertTrue(user.getLoginHistory().isEmpty());
        assertFalse(user.getIsBanned());
        assertFalse(user.getIsAdmin());
    }

    @Test(timeout = 50)
    public void testUser2(){
        ArrayList<Timestamp> newLogin = new ArrayList<>();
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        newLogin.add(dateTime);
        User user = new User("amy", "1234567890", newLogin, true, true);
        assertEquals("amy", user.getUsername());
        assertEquals("1234567890", user.getPassword());
        assertEquals(user.getLoginHistory(), newLogin);
        assertTrue(user.getIsBanned());
        assertTrue(user.getIsAdmin());
    }


    // test setUsername
    @Test
    public void testSetUsername() {
        User user = new User("lindsey", "20220612");
        user.setUsername("lindseyS");
        assertEquals(user.getUsername(),"lindseyS");
    }


    // test setPassword
    @Test
    public void testSetPassword() {
        User user = new User("lindsey", "20220612");
        user.setPassword("1234567890");
        assertEquals("1234567890", user.getPassword());

    }


    // test addLogin
    @Test
    public void testSetLoginHistory() {
        User user = new User("lindsey", "20220612");
        ArrayList<Timestamp> newLogin = new ArrayList<>();
//        Timestamp dateTime = new Timestamp(2022, 6, 12, 8, 22, 34, 10);
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        newLogin.add(dateTime);
        user.setLoginHistory(newLogin);
        assertTrue(user.getLoginHistory().contains(dateTime));
    }


    // test setBanned
    @Test
    public void testSetBanned() {
        User user = new User("lindsey", "20220612");
        user.setBanned(true);
        assertTrue(user.getIsBanned());
    }


    // test setIsAdmin
    @Test
    public void testSetIsAdmin() {
        User user = new User("lindsey", "20220612");
        user.setIsAdmin(true);
        assertTrue(user.getIsAdmin());
    }

    // test addLogin
    @Test
    public void testAddLogin() {
        User user = new User("lindsey", "20220612");
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        user.addLogin(dateTime);
        assertTrue(user.getLoginHistory().contains(dateTime));
    }

}