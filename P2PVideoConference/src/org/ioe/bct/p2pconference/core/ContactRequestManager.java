/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;


import javax.swing.JOptionPane;
import org.ioe.bct.p2pconference.core.db.DBHandler;
import org.ioe.bct.p2pconference.dataobject.Request;
import org.ioe.bct.p2pconference.patterns.observer.DisplayElement;
import org.ioe.bct.p2pconference.patterns.observer.Observer;
import org.ioe.bct.p2pconference.patterns.observer.Subject;
import org.ioe.bct.p2pconference.ui.AppMainFrame;
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

    public void deleteContactRequest(Object body) {
        //break the connection to the peer.
         Request request=(Request) body;
        try {
              DBHandler handler=new DBHandler();
            handler.initConnection();
            String userName=AppMainFrame.getUserName();
            String contact=request.getTo();
            handler.deleteContact(userName, contact);
            JOptionPane.showMessageDialog(null,"Contact has been deleted.");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error on deleting the contact. "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendAddRequestMessage(Object body) {
        Request request=(Request) body;
       
        System.out.println("Sending request message... to the peer "+request.getTo()+"..."+request.getMessage());
        try {
              DBHandler handler=new DBHandler();
            handler.initConnection();
            String userName=AppMainFrame.getUserName();
            String contact=request.getTo();
            handler.addContact(userName, contact);
             JOptionPane.showMessageDialog(null,"Contact request has been sent.");
        }
        catch (Exception e) {
          JOptionPane.showMessageDialog(null,"Error on adding the contact. "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void display() {
       System.out.println("To be implementated..");
       //Record the message on Log and display on outputstream;
    }


}
