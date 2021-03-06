package User;

import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by scottparsons on 2/25/16.
 */
public class UserTest
{
    @org.junit.Test
    public void Should_Get_Count_Of_Users() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        int expectedCount = 5;
        int actualCount = userRepository.getNumberOfUsers();

        assertTrue(actualCount >= actualCount);
    }

    @org.junit.Test
    public void Should_Check_If_Name_Exists_In_Db() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        boolean isCorrectNameInDb = userRepository.userExists("Alex");
        boolean isIncorrectNameInDb = userRepository.userExists("Garcia");

        assertTrue(isCorrectNameInDb);
        assertFalse(isIncorrectNameInDb);
    }

    @org.junit.Test
    public void Should_Not_Add_Already_Existing_Record() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();

        boolean isRecordedAdded = userRepository.createNewUser("Alex", "alex@stanford.edu", "password", true);
        assertFalse(isRecordedAdded);
    }

    @org.junit.Test
    public void Should_Add_And_Delete_User() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        userRepository.createNewUser("TestUserShouldNotExistInDatabaseEver", "test@stanford.edu", "password", true);

        boolean isRecordInDb = userRepository.userExists("TestUserShouldNotExistInDatabaseEver");
        assertTrue(isRecordInDb);

        userRepository.DeleteUser("TestUserShouldNotExistInDatabaseEver");
        isRecordInDb = userRepository.userExists("TestUserShouldNotExistInDatabaseEver");
        assertFalse(isRecordInDb);
    }

    @org.junit.Test
    public void Should_Create_Users_And_Check_If_They_Are_Admin() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        boolean isUserAdmin;

        userRepository.PopulateCurrentUser("Connie");
        isUserAdmin = userRepository.isAdmin();
        assertTrue(isUserAdmin);

        userRepository.PopulateCurrentUser("Scott");
        isUserAdmin = userRepository.isAdmin();
        assertFalse(isUserAdmin);

        userRepository.PopulateCurrentUser("NotRealUserName");
        isUserAdmin = userRepository.isAdmin();
        assertFalse( isUserAdmin);
    }

    @org.junit.Test
    public void Should_Add_Friend_One_Way() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        userRepository.PopulateCurrentUser("Connie");

        userRepository.addFriend(5);
        boolean doesFriendshipExist = userRepository.FriendshipExists(5);
        boolean selfFriendshipShouldNotExist = userRepository.FriendshipExists(2);

        assertTrue(doesFriendshipExist);
        assertFalse(selfFriendshipShouldNotExist);
    }

    @org.junit.Test
    public void Should_Add_Friend_Two_Ways() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        userRepository.PopulateCurrentUser("John");
        userRepository.addFriend(4);

        boolean doesFriendshipExist = userRepository.FriendshipExists(4);

        userRepository.PopulateCurrentUser("Larry");
        boolean doesFriendshipExistOtherWay = userRepository.FriendshipExists(5);

        boolean selfFriendshipShouldNotExist = userRepository.FriendshipExists(4);

        assertTrue(doesFriendshipExist);
        assertTrue(doesFriendshipExistOtherWay);
        assertFalse(selfFriendshipShouldNotExist);
    }

    @org.junit.Test
    public void Should_Add_And_Delete_Friend() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        userRepository.PopulateCurrentUser("Scott");
        userRepository.addFriend(4);
        boolean doesFriendshipExist = userRepository.FriendshipExists(4);

        userRepository.removeFriend(4);
        boolean isScottFriendsWithLarry = userRepository.FriendshipExists(4);

        userRepository.PopulateCurrentUser("Larry");
        boolean isLarryFriendswithScott = userRepository.FriendshipExists(3);

        assertTrue(doesFriendshipExist);
        assertFalse(isScottFriendsWithLarry);
        assertFalse(isLarryFriendswithScott);
    }

    @org.junit.Test
    public void Should_Add_And_Delete_Achievement() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        userRepository.PopulateCurrentUser("Scott");
        userRepository.addAchievement("QuizTaker", "You are the taker of quizzes.");

        boolean doesAchievementExist = userRepository.AchievementExists("QuizTaker");
        assertTrue(doesAchievementExist);

        userRepository.removeAchievement("QuizTaker");
        doesAchievementExist = userRepository.AchievementExists("QuizTaker");
        assertFalse(doesAchievementExist);
    }

    @org.junit.Test
    public void Should_Get_All_Users() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        List<User> userList = userRepository.getAllUsers();

        assertTrue(userList.size() >= 5);
    }

    @org.junit.Test
    public void Should_Correctly_Check_Login() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        userRepository.createNewUser("PasswordTestUser", "test@stanford.edu", "password", true);

        boolean isUserLoginCorrect = userRepository.isCorrectLogin("PasswordTestUser", "password");
        userRepository.DeleteUser("PasswordTestUser");

        assertTrue(isUserLoginCorrect);
    }

    @org.junit.Test
    public void Should_Promote_Standard_User_To_Admin() throws SQLException
    {
        IUserRepository userRepository = new UserRepository();
        userRepository.DeleteUser("AdminTestUser");
        userRepository.createNewUser("AdminTestUser", "test@stanford.edu", "password", false);
        userRepository.PopulateCurrentUser("AdminTestUser");
        assertFalse(userRepository.isAdmin());

        userRepository.promoteToAdmin("AdminTestUser");
        assertTrue(userRepository.isAdmin());
        userRepository.DeleteUser("AdminTestUser");
    }
}