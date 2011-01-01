/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;


import org.ioe.bct.p2pconference.dataobject.Request;
import org.ioe.bct.p2pconference.patterns.observer.DisplayElement;
import org.ioe.bct.p2pconference.patterns.observer.Observer;
import org.ioe.bct.p2pconference.patterns.observer.Subject;
import org.ioe.bct.p2pconference.utils.Notification;

/**
 *
 * @author kusu
 */
public class ContactRequestManager implements Observer, DisplayElement {
    private Subject peerData;


    public ContactRequestManager(Subject sub) {
        this.peerData=sub;
        peerData.registerObserver(this);
    }

    public void update(Notification type) {
       switch(type.getName())
       {
           case Notification.CONTACT_ADDED:
               sendAddRequestMessage(type.getBody());
               display();
               break;

           case Notification.CONTACT_DELETED:
              deleteContactRequest(type.getBody());
              display();
               break;

           default:
               break;

       }
    }

    public void deleteContactRequest(Object contact) {
        //break the connection to the peer.
           System.out.println("Contact deleted");
    }

    public void sendAddRequestMessage(Object body) {
        Request request=(Request) body;
        //to be implemenatated after peer searching is implementated
        System.out.println("Sending request message... to the peer "+request.getTo()+"..."+request.getMessage());
    }

    public void display() {
       System.out.println("To be implementated..");
       //Record the message on Log and display on outputstream;
    }


}
