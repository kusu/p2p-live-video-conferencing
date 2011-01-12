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
import org.ioe.bct.p2pconference.ui.AppMainFrame;

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
    private CaptureNew captureNew;
    public AudioConference(PeerGroup peerGroup,String Creator,Mediator mediator)
    {
        multicastClient=new MulticastClient(peerGroup, Creator,MODE);
        multicastClient.setPort(C_PORT);
        multicastServer=new MulticastServer(peerGroup, mediator);
        multicastServer.setMode(MODE);
        multicastServer.setPort(S_PORT);
        audioCapture=new Capture();
        captureNew=new CaptureNew();
    }

    public class PublishModuleSpecHandler implements Runnable{
        public void run()
        {
            while(true){
                System.out.println("Publishing Pipe Advertisement for the multicast audio conferecing");
                multicastClient.publishPipeAdvertisement();
                //Publishing Audio Module Speculation  at interval of 20 secs??????
                sleep(2000);
            }
        }

    }
    public class DiscoveryAdvertisementHandler implements Runnable{
        public void run()
        {
            while(true)
            {
                System.out.println("Discovering Pipe Advertisement for the multicast audio conferecing");
                multicastServer.getAllMulticastSocketFromPipeAdvertisements();
                //Get all the Audio Module Speculation Published at interval of 20 secs???
                sleep(2000);
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

            System.out.println("Receiving audio streams from remote peers in conferencing");
            ArrayList<PeerAdvertisement> peerAdvArrayList=protectedPG.getConnectedUsers();
            Iterator<PeerAdvertisement> itr=peerAdvArrayList.iterator();
            while(itr.hasNext())
            {
                String peerName=itr.next().getName();
                if(!peerName.equalsIgnoreCase(AppMainFrame.getUserName()))
                {
                System.out.println(peerName+ " Receiving audio buffer");
                byte buffer[]=new byte[8192];  //arbitray ho ..calculation garnu parcha
                multicastServer.receive(peerName, buffer);
                audioCapture.setData(buffer);
                }
            }
                sleep(100);
            }
        }
    }
public class AudioCaptureBeginThread implements Runnable{
    public void run()
    {
        captureNew.captureAudio();
    }
}
    public class SendMessageHandler implements Runnable{
        public void run()
        {
            
            while(true)
            {
                System.out.println("Sending audio streams to remote peers in conferencing");
                byte msg[]=captureNew.getCapturedData();
                multicastClient.sendMesssage(msg);
                sleep(100);
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
