/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.util.HashMap;
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
               peerMsgReceiver.findAdvertisement(sender, "Name", sender+me+"pipe");
               hashingMapInputPipe.put(sender, peerMsgReceiver);
            }
        };
        new Thread(running).start();
       
    }
    public void addReceiver(final String receiver)
    {
        Runnable runing=new Runnable() {

            public void run() {
                hashingMapOutputPipe.put(receiver, peerMsgSender.createOutputPipe(me, receiver));
            }
        };
        new Thread(runing).start();
       
    }
    public void sendDataToReceiver(String data,String receiver)
    {
        peerMsgSender.sendData(data, hashingMapOutputPipe.get(receiver));
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
