/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui.controls;

import java.util.ArrayList;
import javax.swing.JList;
import org.ioe.bct.p2pconference.patterns.observer.Observer;
import org.ioe.bct.p2pconference.patterns.observer.Subject;
import org.ioe.bct.p2pconference.utils.Notification;

/**
 *
 * @author kusu
 */
public class PeerList implements Subject {

     protected ArrayList observers;
      private JList listUI;

     public PeerList() {
         observers=new ArrayList();
     }

   public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        int index=observers.indexOf(o);
        observers.remove(index);
    }

    public void notifyObservers(Notification notificationName) {
         for(int i=0;i<observers.size();i++) {
            Observer o=(Observer)observers.get(i);
            //notify all the observers that new contact has been added.
            o.update(notificationName);
        }
    }

    public JList createList() {
        if(listUI==null) {
            return new JList();
        }
        else {
            return listUI;
        }
    }

}
