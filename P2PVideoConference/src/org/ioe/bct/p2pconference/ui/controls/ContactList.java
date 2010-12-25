/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui.controls;

import java.util.ArrayList;


import org.ioe.bct.p2pconference.utils.Notification;

/**
 *
 * @author kusu
 */
public class ContactList extends PeerList {
    
   
    private ArrayList userList;

    public ContactList(){
       
        userList=new ArrayList();
    }

    

   public void addContact(Object body) {
       Notification note=new Notification();
      //add new peer to the peerlist
       userList.add(body);

       note.setName(Notification.CONTACT_ADDED);
       note.setBody(body);
       notifyObservers(note);
   }

    

    
}
