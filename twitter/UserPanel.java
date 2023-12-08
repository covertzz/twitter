/*
 * Author: Palmer Du 
 * Description: This file handles the creation and function of user panels. Multiple user panels can be created,
 * unlike the Admin Panel.
 */
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class UserPanel {
    twitter twitter = new twitter();
    User user;
    private JFrame u = new JFrame();  
    private JTextArea followUsertf = new JTextArea();
    private JButton followUser = new JButton("Follow User");
    private JTextArea following = new JTextArea("Current Following:\n");
    private JTextArea posttf = new JTextArea();
    private JButton post = new JButton("Post");
    private JTextArea newsFeed = new JTextArea("News Feed:\n");
    private JLabel timeOfCreation = new JLabel("Time of Creation");
    private JLabel timeLastUpdated = new JLabel("Time Last Updated");

    public UserPanel(User uc) {
        user = uc;
        u.setTitle(user.getID() + " User Panel ");
        timeOfCreation.setText("Time of Creation: " + user.getTimeOfCreation());
    }
    public void build() {
        setListeners();
        setBounds();
        addElements();
        
        u.setVisible(true);  
    }

    public void follow() {
        String newFollow = followUsertf.getText();
        for (Map.Entry<String, User> e: twitter.getInstance().allUsers.entrySet()) {
            if(e.getKey().equals(newFollow)) {
                if (!user.getFollowing().contains(newFollow)) {
                    user.follow(newFollow);
                    following.append(newFollow+"\n");
                }
                else{System.out.println("User already followed");}
            } 
            else {
                System.out.println("No such user exists");
            }
        }
    }

    public void post() {
        user.post(posttf.getText()); 
        AdminPanel.UpdateUserPanels(this.user);
    } 
    public void updateNewsFeed(User postingUser) {
        newsFeed.append(" - " + postingUser.getID()+ ": " + this.user.getNewsFeed().get(this.user.getNewsFeed().size()-1) + "\n");
        timeLastUpdated.setText("Time Last Updated: " + user.getTimeLastUpdated());
    }

    private void setListeners() {
        followUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                follow();
            }
        });
        post.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                post();
            }
        });
    }

    private void setBounds() {
        followUser.setBounds(220, 10, 200, 30);
        followUsertf.setBounds(10, 10, 200, 30);
        following.setBounds(10, 50, 420, 300);
        post.setBounds(320, 360, 100, 30);
        posttf.setBounds(10, 360, 300, 30);
        newsFeed.setBounds(10, 400, 420, 300);
        timeLastUpdated.setBounds(10, 710, 200, 30);
        timeOfCreation.setBounds(220, 710, 200, 30);

        u.setSize(500,1000);  
        u.setLayout(null);  
    }

    private void addElements() {
        u.add(followUser);u.add(followUsertf);u.add(following);
        u.add(post);u.add(posttf);u.add(newsFeed);u.add(timeLastUpdated);u.add(timeOfCreation);
    }
}
