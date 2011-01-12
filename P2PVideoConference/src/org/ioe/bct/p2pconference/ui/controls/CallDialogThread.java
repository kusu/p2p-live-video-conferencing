/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JWindow;
import org.ioe.bct.p2pconference.AppLoader;
import org.ioe.bct.p2pconference.patterns.mediator.Mediator;
import org.ioe.bct.p2pconference.ui.CallPanel;

/**
 *
 * @author kusu
 */
public class CallDialogThread extends  Thread {

    boolean looper=true;
    JWindow callWindow;
    CallPanel panel;
    Mediator mediator;
    public CallDialogThread(Mediator m) {
        callWindow=new JWindow(AppLoader.mainFrame);
//        callWindow.setTitle("Voice Call..");
        panel=new CallPanel();
        panel.getEndButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stopThread("Call Cancelled...");
            }
        });
        callWindow.getContentPane().add(panel);
        callWindow.setBounds(500, 300, 300, 50);
        callWindow.pack();
       
        callWindow.setVisible(true);
        this.mediator=m;
    }

    @Override
    public void run() {
    int count=1;
        while (looper) {
//            panel.setVisible(false);
            panel.appendDots(count);
//            panel.setVisible(true);
            count++;
            if(count>3) {
                count=0;
            }
            pause(1000);
        }
    }

    public void pause(long time) {
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
    public void stopThread(String message) {
        looper=false;
        panel.showMessage(message);
       // pause(3000);
        mediator.sendMessage(ConferenceMediator.PRIVATE_CALL_END_SYNC, null, ConferenceMediator.PRIVATE_CALL_END_SYNC_CODE);
        callWindow.dispose();
    }
    


public static void main(String[] args){
  
}
}
