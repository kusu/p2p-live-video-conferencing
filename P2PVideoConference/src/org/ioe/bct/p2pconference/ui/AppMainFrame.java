/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AppMainFrame.java
 *
 * Created on Nov 5, 2010, 11:10:31 AM
 */

package org.ioe.bct.p2pconference.ui;

import javax.swing.event.ChangeEvent;
import org.ioe.bct.p2pconference.ui.controls.ConferenceMediator;
import org.ioe.bct.p2pconference.ui.controls.LoginDialog;
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import net.jxta.platform.NetworkManager.ConfigMode;
import org.ioe.bct.p2pconference.core.JXTAPeerGroupOrganizer;
import org.ioe.bct.p2pconference.core.P2PNetworkCore;
import org.ioe.bct.p2pconference.core.PeerGroupOrganizer;
import org.ioe.bct.p2pconference.prototype.patterns.mediator.Mediator;

import org.ioe.bct.p2pconference.ui.controls.ContactList;
import org.ioe.bct.p2pconference.prototype.patterns.observer.Subject;

/**
 *
 * @author kusu
 */
public class AppMainFrame extends javax.swing.JFrame {
   
    /** Creates new form AppMainFrame */
    public AppMainFrame() {
        super("P2P Video Conferencing...");
        initComponents();
        loadConferencePanel();
    }

    public void disposeLoginBox() {
        loginBox.dispose();
    }

    public void createMenus() {
        menubar=new JMenuBar();
        JMenu contactMenu=new JMenu("Contacts");
        JMenuItem addContactMenu=new JMenuItem("Add Contact");
        JMenuItem createGroupMenu=new JMenuItem("Create Group");

        addContactMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        createGroupMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createGroupDialogActionHandler();
            }
        });



        contactMenu.add(addContactMenu);
        contactMenu.add(createGroupMenu);

        setJMenuBar(menubar);
        menubar.add(contactMenu);
        menuHash.put("contacts",contactMenu);
        
    }

    private void createGroupDialogActionHandler() {
         
         CreateGroupDialog cgDialog=null;
               
                   

                   cgDialog=new CreateGroupDialog(this,groupOrganizer,confMediator);
                    
                    //cgDialog.setBounds(150, 150, 400, 150);
                    cgDialog.setVisible(true);
                    
                   // cgDialog.dispose();
               
              
            }


    public void addContact() {
       
        SearchContactDialog searchContact=new SearchContactDialog(this, true);
       searchContact.setVisible(true);

    }

    public void hideLoginBox() {
        loginBox.setVisible(false);
    }

    public void loadLoginBox() {
        if(loginBox==null) {
            loginBox=new LoginDialog(this, "Login..").init();

        }
        loginBox.setVisible(true);
    }

    public AppMainFrame load(String userName,String pass) {
        this.username=userName;
        this.password=pass;
        createMenus();
        setBounds(150, 100, 900, 600);
        

        return this;
    }

    public void loadContactList() {
         
        contListPanel=new ContactListPanel(confMediator).setList(contactList);
        contListPanel.setMediator(confMediator);
        contGroupPanel=new GroupsPanel(confMediator);
        contGroupPanel.setMediator(confMediator);
       
       contactListTab=new JTabbedPane(JTabbedPane.TOP);
        contactListTab.addTab("Contacts", contListPanel);
        contactListTab.addTab("Groups", contGroupPanel);
        
        contactPanel.add(contactListTab,BorderLayout.CENTER);
        jSplitPane1.setDividerLocation(200);
       contactListTab.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if(contactListTab.getSelectedComponent().equals(contListPanel)) {
                    contListPanel.sendSelectionChangeMsg();
                }
                else if(contactListTab.getSelectedComponent().equals(contGroupPanel)) {
                    contGroupPanel.sendSelectionChangedMessage();
                }
            }
        });
        
    }

    public void createNetworkCore(String name) {
        netCOre=new P2PNetworkCore(name);
        netCOre.startNetwork(ConfigMode.ADHOC);
        System.out.println("creating network core");
         contGroupPanel.startGroupDiscovery();
    }

    

    private void loadConferencePanel() {
        appConfPanel=new ConferencePanel();
        appConfPanel.setMediator(confMediator);
       
        contactDetialsPanel.add(appConfPanel,BorderLayout.CENTER);
        contactDetialsPanel.revalidate();
        jSplitPane1.revalidate();
        validateTree();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        contactPanel = new JPanel(new BorderLayout());
        ;
        contactDetialsPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        contactPanel.setMinimumSize(new java.awt.Dimension(100, 100));
        loadContactList();
        jSplitPane1.setLeftComponent(contactPanel);

        contactDetialsPanel.setBackground(new java.awt.Color(204, 255, 255));
        contactDetialsPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setRightComponent(contactDetialsPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
   
    public Subject getContactList() {
        return contactList;
    }

    private PeerGroupOrganizer groupOrganizer=new JXTAPeerGroupOrganizer();
    public static P2PNetworkCore netCOre;
    private ConferencePanel appConfPanel;
    private Mediator confMediator=new ConferenceMediator();
    private String username;
    private String password;
    private static LoginDialog loginBox;
    private JMenuBar menubar;
    private HashMap menuHash=new HashMap();
    
    private Subject contactList=new ContactList();
    private ContactListPanel contListPanel;
    private GroupsPanel contGroupPanel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contactDetialsPanel;
    private javax.swing.JPanel contactPanel;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables
    private JTabbedPane contactListTab;
}
