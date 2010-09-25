/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.data;

/**
 *
 * @author kusu
 */
public class UserInfo {
    private String userName="";
    private String passWord="";
    private String emailId="";
    
    public UserInfo(){
        
    }
    public UserInfo(String uname,String pw,String email){
        this.userName=uname;
        this.passWord=pw;
        this.emailId=email;
    }

    public String getEmailId(){
        return emailId;
    }

    public void setEmailId(String emailId){
        this.emailId=emailId;
    }

    public void setUsername(String uname){
        this.userName=uname;
    }

    public String getUsername(){
        return userName;
    }

    public void setPassword(String passw){
        this.passWord=passw;
    }

    public String getPassword(){
        return  passWord;
    }

}
