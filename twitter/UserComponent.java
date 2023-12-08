/*
 * Author: Palmer Du 
 * Description: This file includes the User and Group objects. This file follows the visitor design patters.
 * Users and Groups follow the composite design structure.They are both implemented from UserComponent, and 
 * have common methods like getID() to return its ID and hasChild(String ID) to check if a certain group 
 * contains an ID. Additionally, user's news feeds follow an observer design pattern, where user's posts 
 * will automatically be sent to that user's followers.
 */
import java.util.*;

public interface UserComponent {
    public void add(UserComponent newUserComponent);
    public String getID();
    public boolean hasChild(String ID);
    void accept(UserVisitor uv);
    int getCount(UserVisitor uv);
    int getPositive(UserVisitor uv);
    long getTimeOfCreation();
    long getTimeLastUpdated();
}

//leaf
class User implements UserComponent{ 
    private static twitter twitter = new twitter();

    private String ID = "";
    private ArrayList<String> followers = new ArrayList<String>();
    private ArrayList<String> following = new ArrayList<String>();
    private ArrayList<String> newsFeed = new ArrayList<String>();
    private long timeOfCreation = 0;
    private long timeLastUpdated = 0;

    
    public User(String ID){
        this.ID = ID;
        timeOfCreation = System.currentTimeMillis();
    }
    public String getID(){return ID;}
    public void setID(String ID){this.ID = ID;}
    public ArrayList<String> getFollowers() {return followers;}
    public ArrayList<String> getFollowing() {return following;}
    public ArrayList<String> getNewsFeed() {return newsFeed;}
    public long getTimeOfCreation() {return timeOfCreation;}
    public long getTimeLastUpdated() {return timeLastUpdated;}

    public void add(UserComponent newUserComponent) {
        System.out.print("you cannot add a user component to a user!");
    }
    public void follow(String ID){
        following.add(ID);
        twitter.getInstance().allUsers.get(ID).addNewFollower(this.ID);
    }
    public void addNewFollower(String ID) {
        followers.add(ID);
    }
    public void post(String post) { //observer
        newsFeed.add(post);
        for(String s : followers) {
            twitter.getInstance().allUsers.get(s).updateNewsFeed(post);
        }
        timeLastUpdated = System.currentTimeMillis();
    }
    public void updateNewsFeed(String post) { //observer
        newsFeed.add(post);
    }
    public boolean hasChild(String ID) {
        if(this.ID.equals(ID)){
            return true;
        }
        return false;
    }
    public int getCount(UserVisitor uv) {
        return uv.getCount(this);
    }
    public int getPositive(UserVisitor uv) {
        return uv.getPositive(this);
    }
    public void accept(UserVisitor uv) { //visitor
        uv.getID(this);
    }
    @Override
    public String toString() { //to display ID in Java swing node
        return this.ID;
    }
}

//composite
class UserGroup implements UserComponent{
    String ID; 
    private HashMap<String, UserComponent> stuffInGroup = new HashMap<String, UserComponent>();
    private long timeOfCreation = System.currentTimeMillis();

    public UserGroup(String GroupId) {this.ID = GroupId;}
    public String getID(){return ID;}
    public String getID(String ID) {return stuffInGroup.get(ID).getID();}
    public void setID(String ID){this.ID = ID + "G";}
    public long getTimeOfCreation() {return timeOfCreation;}
    public long getTimeLastUpdated() {return 0;}

    public void add(UserComponent newUserComponent) {
        stuffInGroup.put(newUserComponent.getID(), newUserComponent);
    }
    public HashMap<String, UserComponent> getStuffInGroup() {
        return stuffInGroup;
    }
    public boolean hasChild(String ID) {
        if(this.ID.equals(ID)) {
            return true;
        }
        for(HashMap.Entry<String,UserComponent> h : stuffInGroup.entrySet()) {
            if(h.getKey().equals(ID)) {
                return true;
            }
        }
        return false;
    }
    public int getCount(UserVisitor uv) {
        return uv.getCount(this);
    }
    public int getPositive(UserVisitor uv) {
        return uv.getPositive(this);
    }
    public void accept(UserVisitor uv) { //visitor
        uv.getID(this);
    }
    @Override
    public String toString() { //to display ID in Java swing node
        return this.ID;
    }
}


