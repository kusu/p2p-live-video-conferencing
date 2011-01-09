/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.jxta.pipe.OutputPipe;
import org.ioe.bct.p2pconference.patterns.mediator.Mediator;

/**
 *
 * @author Administrator
 */
public class PrivateMsgManager {
    private String me;
    private HashMap<String,OutputPipe> hashingMapOutputPipe=new HashMap<String, OutputPipe>();
    private HashMap<String,PeerMsgReceiver> hashingMapInputPipe=new HashMap<String, PeerMsgReceiver>();
    private HashMap<String,Boolean> hashingPublishParameter=new HashMap<String, Boolean>();
    private PeerMsgSender peerMsgSender;
    private PeerMsgReceiver peerMsgReceiver;
    private P2PNetworkCore netCore;
    private Mediator mediator;
    public PrivateMsgManager(String me,P2PNetworkCore netCore,Mediator m)
    {
        this.me=me;
        mediator=m;
        this.netCore=netCore;
        peerMsgSender=new PeerMsgSender(netCore);
        

    }
    public void addSender(final String sender)
    {
        Runnable running=new Runnable() {

            public void run() {
               peerMsgReceiver=new PeerMsgReceiver(netCore, mediator);
              peerMsgReceiver.buildModuleAdvertisement();
              peerMsgReceiver.buildModuleSpecificationAdvertisement(peerMsgReceiver.createPipeAdvertisement(me, sender),me+sender+"modulespecadd");
               hashingMapInputPipe.put(sender, peerMsgReceiver);
                }
        };
        new Thread(running).start();
       
    }
    public void addReceiver(final String receiver)
    {
        Runnable runing=new Runnable() {

            public void run() {
                OutputPipe oppipe=null;
                while(oppipe==null)
                {
                    try {
                        System.out.println("Discovering the advertisment of inputPipe to create outputPipe");
                        oppipe = peerMsgSender.findAdvertisement(receiver, "Name", receiver + me + "modulespecadd");
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PrivateMsgManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                hashingMapOutputPipe.put(receiver,oppipe);
                JOptionPane.showMessageDialog(null, "Successfully added an entry to hashMap\nThe size now is"+hashingMapOutputPipe.size()+"\n"+hashingMapOutputPipe.toString());
            }
        };
        new Thread(runing).start();
       
    }

    public void publishAdvertisement(final String sender)
    {
        Runnable running=new Runnable(){
          public void run()
            {
              boolean continueExecution=true;
              while(continueExecution)
              {
              if(hashingMapInputPipe.containsKey(sender))
              {
                  System.out.println("Publishing input pipe for "+sender);
                  PeerMsgReceiver pMsgReceiver=hashingMapInputPipe.get(sender);
                  if(pMsgReceiver.needPulishing)
                        pMsgReceiver.publishAdvertisments();
                  else
                      continueExecution=false;
              }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PrivateMsgManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
              }
          }
        };
        new Thread(running).start();
    }
    public void sendDataToReceiver(String data,String receiver)
    {
        OutputPipe oppipe=hashingMapOutputPipe.get(receiver);
        peerMsgSender.sendData(data, oppipe);
    }

    public void setContactList(String peers[])
    {
      
        for(String peer: peers){
            if(!peer.equals(me)){
                addReceiver(peer);
                addSender(peer);
            }
        }
    }
}
