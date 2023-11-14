/*
 * This file deals with all aspects of the visitor design principle, namely for functions getID, getCount (# of tweets),
 * and getPositive (# of positive tweets).
 */
import java.util.*;

public interface UserVisitor {
    String getID(User user);
    String getID(UserGroup userGroup);
    int getCount(User user);
    int getCount(UserGroup userGroup);
    int getPositive(User user);
    int getPositive(UserGroup userGroup);
}

class UserVisitor1 implements UserVisitor {
    public String getID(User user) {
        return user.getID();
    }
    public String getID(UserGroup userGroup) {
        return userGroup.getID();
    }
    public int getCount(User user) { 
        int count = 0;
        for(int i = 0; i < user.getNewsFeed().size(); i++) {
            count++;
        }
        return count;
    }
    public int getCount(UserGroup userGroup) {
        int sum = 0;
        for(HashMap.Entry<String,UserComponent> h : userGroup.getStuffInGroup().entrySet()) {
            sum = sum + h.getValue().getCount(this);
        }
        return sum;
    }
    public int getPositive(User user) {
        int count = 0;
        for(String s : user.getNewsFeed()) {
            if(s.contains("good") || s.contains("excellent") || s.contains("great")) {
                count++;
            }
        }
        return count;
    }
    public int getPositive(UserGroup userGroup) {
        int sum = 0;
        for(HashMap.Entry<String,UserComponent> h : userGroup.getStuffInGroup().entrySet()) {
            sum = sum + h.getValue().getPositive(this);
        }
        return sum;
    }
}