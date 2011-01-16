/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.ioe.bct.p2pconference.dataobject.User;

/**
 *
 * @author kusu
 */
public class DBHandler {

    private DBConnector myDBConnector;

    public DBHandler() {
        myDBConnector=MYSQLDBConnector.getInstance("p2puser");
       
    }
    public void initConnection() throws Exception {
        myDBConnector.initialize();
    }

    public boolean createUser(String username,String password,String email) throws SQLException,ClassNotFoundException {
         myDBConnector.connect();
        if(checkExistence(username)) {
            JOptionPane.showMessageDialog(null, "User already exists.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String sql="insert into userinfo values (?,?,?)";
        PreparedStatement stm=myDBConnector.getStatement(sql);
        stm.setString(1, username);
        stm.setString(2, email);
        stm.setString(3, password);
        stm.executeUpdate();
        myDBConnector.commit();
        myDBConnector.close();
        return true;
    }

    public ArrayList<User> searchContact(String cread) throws SQLException,ClassNotFoundException{
         myDBConnector.connect();
        ArrayList<User> resultArray=new ArrayList<User>();
        String sql="select* from userinfo where username like ? or email like ?";
        PreparedStatement stm=myDBConnector.getStatement(sql);
        stm.setString(1, cread+"%");
        stm.setString(2, cread+"%");
        ResultSet result=stm.executeQuery();
        while (result.next()) {
            String userName=result.getString("username");
            String email=result.getString("email");
            System.out.println(userName);
            User u=new User(userName, email);
            resultArray.add(u);
        }
         myDBConnector.close();
        return resultArray;
    }

    public boolean checkExistence(String user) throws SQLException{
        
        String sql="select* from userinfo where username=?";
        PreparedStatement stm=myDBConnector.getStatement(sql);
        stm.setString(1,user);
        ResultSet result=stm.executeQuery();
        if(result.next()) {
            return true;
        }
         
        return false;
    }

    public boolean checkLogin(String user,String password) throws SQLException, ClassNotFoundException{
        myDBConnector.connect();
        String sql="select password from userinfo where username=?";
        PreparedStatement stm=myDBConnector.getStatement(sql);
        stm.setString(1,user);
        ResultSet result=stm.executeQuery();
       
        if (result.first()) {
        String pw=result.getString("password");
        if(pw.equals(password)) {
            return true;

        }
         else {
                    return false;
         }
        }
        return false;
    }

    public void commitTranscation() throws Exception{
        myDBConnector.commit();
    }

    public void rollbackTransation() throws Exception{
        myDBConnector.rollback();
    }

    public void close() throws Exception {
        myDBConnector.close();
    }
    public ArrayList<String> getContacts(String userName) throws SQLException,ClassNotFoundException {
        ArrayList<String> userList=new  ArrayList<String>();
        myDBConnector.connect();
        String sql="select contacts from usercontact where username=?";
        PreparedStatement stm=myDBConnector.getStatement(sql);
        stm.setString(1,userName);
        System.out.println(userName);
        ResultSet result=stm.executeQuery();
        while(result.next()) {
            String contact=result.getString("Contacts");
            userList.add(contact);
        }
        return userList;
    }

    public void addContact(String userName,String contactName) throws SQLException, ClassNotFoundException{
        myDBConnector.connect();
        String sql="insert into usercontact values (?,?)";
        PreparedStatement stm=myDBConnector.getStatement(sql);
        stm.setString(1,userName);
        stm.setString(2, contactName);
        stm.executeUpdate();
        myDBConnector.commit();
        myDBConnector.close();
    }

    public void deleteContact(String userName,String contactName) throws SQLException,ClassNotFoundException {
         myDBConnector.connect();
        String sql="delete from usercontact where username=? and contacts=?";
        PreparedStatement stm=myDBConnector.getStatement(sql);
        stm.setString(1,userName);
        stm.setString(2, contactName);
        stm.executeUpdate();
        myDBConnector.commit();
        myDBConnector.close();
    }
}
