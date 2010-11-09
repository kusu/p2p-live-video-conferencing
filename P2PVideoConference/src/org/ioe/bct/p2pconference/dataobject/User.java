/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.dataobject;

/**
 *
 * @author kusu
 */
public class User {
    private String name;
    private String email;
    private int status;

    public User(String name,String email) {
        this.name=name;
        this.email=email;
    }

    public void setName(String name ){
        this.name=name;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public void setStatus(int status){
        this.status=status;
    }

    public String getName() {return name;}
    public String getEmail() {return email;}
    public int getStatus() {return status;}

}
