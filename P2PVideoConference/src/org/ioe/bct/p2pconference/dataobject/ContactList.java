/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.dataobject;

import java.util.ArrayList;
import org.ioe.bct.p2pconference.prototype.pattern.observer.Observer;
import org.ioe.bct.p2pconference.prototype.pattern.observer.Subject;
import org.ioe.bct.p2pconference.utils.Notification;

/**
 *
 * @author kusu
 */
public class ContactList implements Subject {
    
    private ArrayList observers;
    private ArrayList userList;

    public ContactList(){
        observers=new ArrayList();
        userList=new ArrayList();
    }

    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        int index=observers.indexOf(o);
        observers.remove(index);
    }

   public void addContact(Object body) {
       Notification note=new Notification();
      //add new peer to the peerlist
       userList.add(body);

       note.setName(Notification.CONTACT_ADDED);
       note.setBody(body);
       notifyObservers(note);
   }

    public void notifyObservers(Notification notificationName) {
         for(int i=0;i<observers.size();i++) {
            Observer o=(Observer)observers.get(i);
            //notify all the observers that new contact has been added.
            o.update(notificationName);
        }
    }

}
