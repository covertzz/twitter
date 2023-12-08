/*
 * Author: Palmer Du
 * Description: This file handles the creation of the Admin Panel GUI, which is the main method of interacting
 * with this program. The Admin Panel follows a singleton design pattern, meaning that only one instance of it may 
 * be created and referenced.
 */
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;

public class AdminPanel {
    private static twitter twitter = new twitter();
    private static JFrame f=new JFrame("Tweeter");  
    private static DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
    private static JTree tree = new JTree(rootNode);;
    private static JButton addNewUser = new JButton("Add User"); ; 
    private static JTextField addNewUsertf = new JTextField(); ;
    private static JButton showUserTotal=new JButton("Show User Total");
    private static JButton addNewGroup=new JButton("Add Group");  
    private static JTextField addNewGrouptf = new JTextField();  
    private static JButton showGroupTotal=new JButton("Show Group Total");  
    private static JTextArea panel = new JTextArea();;
    private static UserGroup root = twitter.createUserGroup("Root");
    private static JLabel selectedLabel = new JLabel();
    private static JButton openUserView = new JButton("Open User View");
    private static JButton showCount=new JButton("Show Total Number of Tweets"); 
    private static JButton showPositive=new JButton("Show Percentage of Positive Tweets"); 
    private static JButton validateIDS=new JButton("Validate IDs");
    private static JButton lastUpdatedUser = new JButton("Show Last Updated User");
    private static ArrayList<UserPanel> userPanels = new ArrayList<UserPanel>();
    //singleton 
    public static AdminPanel instance;
    public AdminPanel() {}
    public static AdminPanel getInstance() {
        if (instance==null) {
            instance = new AdminPanel();
        }
        return instance;
    }

    public void build() {
        setListeners();
        setBounds();
        addElements();
         
        f.setVisible(true);  
    }

    private static void addUser() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        String username = addNewUsertf.getText();
        try {
            if(!username.equals(null)) {
                if(!selectedNode.getUserObject().getClass().equals(User.class)) {
                    User newUser = twitter.getInstance().createUser(addNewUsertf.getText());
                    root.add(newUser);
                    selectedNode.add(new DefaultMutableTreeNode(newUser));
                    panel.setText("User " + newUser.getID() + " added");
                    tree.updateUI();
                }
                else{
                    panel.setText("Error: cant add componenet to users!");
                }
            }
            else {
                panel.setText("Error: no username");
            }
        }
        catch(Exception e) {
            panel.setText("Error, no group selected");
        }
    }

    private static void ShowUserTotal() {
        panel.setText("User Total: " + twitter.getInstance().getNumUsers());
    }

    private static void addGroup() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        String groupname = addNewGrouptf.getText();
        try {
            if(!groupname.equals(null)) {
                if(!selectedNode.getUserObject().getClass().equals(User.class)) {
                    if(!root.hasChild(groupname)){
                        UserGroup newUserGroup = twitter.getInstance().createUserGroup(addNewGrouptf.getText());
                        root.add(newUserGroup);
                        selectedNode.add(new DefaultMutableTreeNode(newUserGroup));
                        selectedNode.setAllowsChildren(true);
                        panel.setText("User Group " + newUserGroup.getID() + " added");
                    }
                    else {
                        panel.setText("Error: user group " + groupname + " may not be added multiple times");
                    }
                    tree.updateUI();
                }
                else{
                    panel.setText("Error: cant add componenet to users!");
                }
            }
            else {
                panel.setText("Error: no groupname");
            }
        }
        catch(Exception e) {
            panel.setText("Error, no group selected");
        }
    }

    private static void ShowGroupTotal() {
        panel.setText("Group Total: " + twitter.getInstance().getNumGroups());
    }

    private static void ShowCount() {
        UserVisitor visitor = new UserVisitor1();
        panel.setText("Total number of tweets: " + String.valueOf(root.getCount(visitor)));
    }

    private static void showPositive() {
        UserVisitor visitor = new UserVisitor1();
        panel.setText("Total percentage of positive tweets:" + String.valueOf(100 * ((float)root.getPositive(visitor)/(float)root.getCount(visitor))) + "%");
    }

    private static void OpenUserView() throws Exception {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        UserComponent uc = (UserComponent)selectedNode.getUserObject();
        
        if(uc.getClass() == User.class) {
            UserPanel userPanel = new UserPanel((User)uc);
            userPanel.build();
            userPanels.add(userPanel);
        }
       
    }

    public static void UpdateUserPanels(User user) {
        for (UserPanel up : userPanels) {
            if (up.user.getFollowing().contains(user.getID()) || up.user.getID() == user.getID()){
                up.updateNewsFeed(user);
            }
        }
    }

    public static void validate() {
        panel.setText("Everything is ok!");
        for (Map.Entry<String, UserComponent> entry : root.getStuffInGroup().entrySet()) {
            int count = 2;
            if(entry.getValue().getID().contains(" ")) {
                panel.setText("Error: the name " + entry.getValue().getID() + " contains a space");
                break;
            }
            if(root.hasChild(entry.getKey())) {
                count--;
                if(count==0) {
                    panel.setText("Error: the name " + entry.getValue().getID() + " is duplicated");
                    break;
                }
            }
        }
    }   

    public static void showLastUpdatedUser() {
        long max = 0;
        User user = null;
        for (Map.Entry<String, UserComponent> entry : root.getStuffInGroup().entrySet()) {
            if(entry.getValue().getTimeLastUpdated() > max) {
                max = entry.getValue().getTimeLastUpdated();
                user = (User)entry.getValue();
            }
        }
        panel.setText(user.getID());
    }

    private static void setListeners() {
        addNewUser.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {addUser();}  
        }); 
        showUserTotal.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {ShowUserTotal();}  
        }); 
        addNewGroup.addActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent e) {addGroup();}
        });
        showGroupTotal.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {ShowGroupTotal();}  
        });
        openUserView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    OpenUserView();
                } 
                catch (Exception ex) {
                    panel.setText("Error: user not selected");
                    System.out.print(ex);
                }
            }
        });
        showCount.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {ShowCount();}  
        });
        showPositive.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {showPositive();}  
        });
        validateIDS.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {validate();}  
        });
        lastUpdatedUser.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {showLastUpdatedUser();}  
        });
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                selectedLabel.setText(selectedNode.getUserObject().toString());
            }
        });
    }

    private static void setBounds() {
        tree.setBounds(10, 10, 300, 450);
        panel.setBounds(320,130, 410,30); 
        addNewUser.setBounds(530,10,200,30);  
        addNewUsertf.setBounds(320,10, 200,30); 
        showUserTotal.setBounds(320,360 ,200,30);  
        addNewGroup.setBounds(530,50,200,30);  
        addNewGrouptf.setBounds(320,50, 200,30);
        showGroupTotal.setBounds(530,360 ,200,30);  
        selectedLabel.setBounds(500, 280, 500, 30);
        openUserView.setBounds(320, 90, 410, 30);
        showCount.setBounds(320,400 ,200,30);
        showPositive.setBounds(530,400 ,200,30);
        validateIDS.setBounds(320, 320, 200, 30);
        lastUpdatedUser.setBounds(530, 320, 200, 30);
        f.setSize(1000,500); 
        f.setLayout(null); 
    }

    private static void addElements() {
        f.add(panel);
        f.add(addNewUser);f.add(addNewUsertf);f.add(showUserTotal);
        f.add(addNewGroup);f.add(addNewGrouptf);f.add(showGroupTotal);
        f.add(tree);f.add(selectedLabel);f.add(openUserView);f.add(showCount);f.add(showPositive);f.add(validateIDS);f.add(lastUpdatedUser);
    }
}
