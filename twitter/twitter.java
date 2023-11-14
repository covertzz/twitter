/*
 * Author: Palmer Du
 * Description: This file handles the creation and tracking of users and groups
 */
import java.util.*;

public class twitter {
    //singleton 
    public static twitter instance;
    public twitter() {}
    public twitter getInstance() {
        if (instance==null) {
            instance = new twitter();
        }
        return instance;
    }

    Map<String, User> allUsers = new HashMap<String, User>();
    Map<String, UserGroup> allGroups = new HashMap<String, UserGroup>();
    
    public User createUser(String username) {
        User newUser = new User(username);
        allUsers.put(newUser.getID(), newUser);
        return newUser;
    }
    public UserGroup createUserGroup(String groupname) {
        UserGroup newGroup = new UserGroup(groupname);
        allGroups.put(newGroup.getID(), newGroup);
        return newGroup;
    }
    public int getNumUsers() {
        return allUsers.size();
    }
    public int getNumGroups() {
        return allGroups.size();
    }
}