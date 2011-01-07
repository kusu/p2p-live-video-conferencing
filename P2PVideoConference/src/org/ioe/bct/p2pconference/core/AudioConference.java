/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PeerAdvertisement;
import org.ioe.bct.p2pconference.dataobject.ProtectedPeerGroup;
import org.ioe.bct.p2pconference.patterns.mediator.Mediator;

/**
 *
 * @author Administrator
 */
public class AudioConference {
    private MulticastClient multicastClient;
    private MulticastServer multicastServer;
    private int C_PORT=9870;
    private int S_PORT=9871;
    private String MODE="conferenceAudioModuleSpec";
    private Capture audioCapture;
    public AudioConference(PeerGroup peerGroup,String Creator,Mediator mediator)
    {
        multicastClient=new MulticastClient(peerGroup, Creator,MODE);
        multicastClient.setPort(C_PORT);
        multicastServer=new MulticastServer(peerGroup, mediator);
        multicastServer.setMode(MODE);
        multicastServer.setPort(S_PORT);
        audioCapture=new Capture();
    }

    public class PublishModuleSpecHandler implements Runnable{
        public void run()
        {
            while(true){
                multicastClient.publishPipeAdvertisement();
                //Publishing Audio Module Speculation  at interval of 20 secs??????
                sleep(20000);
            }
        }

    }
    public class DiscoveryAdvertisementHandler implements Runnable{
        public void run()
        {
            while(true)
            {
                multicastServer.getAllMulticastSocketFromPipeAdvertisements();
                //Get all the Audio Module Speculation Published at interval of 20 secs???
                sleep(20000);
            }
        }
    }
    public class ReceiveMessageHandler implements Runnable{
        ProtectedPeerGroup protectedPG;
        public ReceiveMessageHandler(ProtectedPeerGroup pPG)
        {
            protectedPG=pPG;
        }

        public void run()
        {
            while(true)
            {
            ArrayList<PeerAdvertisement> peerAdvArrayList=protectedPG.getConnectedUsers();
            Iterator<PeerAdvertisement> itr=peerAdvArrayList.iterator();
            while(itr.hasNext())
            {
                String peerName=itr.next().getName();
                byte buffer[]=new byte[16784];  //arbitray ho ..calculation garnu parcha
                multicastServer.receive(peerName, buffer);
                audioCapture.setData(buffer);
            }
                sleep(1000);
            }
        }
    }

    public class SendMessageHandler implements Runnable{
        public void run()
        {
            while(true)
            {
                byte msg[]=audioCapture.getData();
                multicastClient.sendMesssage(new String(msg));
                sleep(1000);
            }
        }
    }
    private void sleep(long millis)
    {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
