/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaMulticastSocket;
import org.ioe.bct.p2pconference.dataobject.TextMessage;
import org.ioe.bct.p2pconference.patterns.mediator.Mediator;
import org.ioe.bct.p2pconference.ui.AppMainFrame;
import org.ioe.bct.p2pconference.ui.controls.ConferenceMediator;

/**
 *
 * @author kusu
 */
public class MulticastServer {
        private HashMap<String,JxtaMulticastSocket> multicasts=new HashMap<String,JxtaMulticastSocket>();
        private ArrayList<PipeAdvertisement> pipeAdvertisements=new ArrayList<PipeAdvertisement>();
        private MulticastSocketService multicastSS=null;
        private PeerGroup peerGroup=null;
        private Mediator mediator;
        private int portAddress=9290;  //default value so as not to alter anything in text multicasting already implemented
        public MulticastServer(PeerGroup peerGroup,Mediator med)
        {
            this.mediator=med;
            this.peerGroup=peerGroup;
            multicastSS=new MulticastSocketService(peerGroup);
        }
        public void setMode(String Mode)    //set the mode for either conferenceAudio or conferenceText(default no need to set explicitly)
        {
            multicastSS.setMode(Mode);
        }

        public void setPort(int port)   //set the port for different variation of application(e.g audio and/or text) of this class
        {
            portAddress=port;
        }
        public PeerGroup getPeerGroup()
        {
            return peerGroup;
        }
        public void getAllMulticastSocketFromPipeAdvertisements()
        {

            //multicasts.clear();
            pipeAdvertisements=multicastSS.getAllLocalPipeAdvertisement();
            for(int i=0;i<pipeAdvertisements.size();i++)
            {
                try {
                    if(!multicasts.containsKey(pipeAdvertisements.get(i).getName()))
                    {
                    JxtaMulticastSocket temp = new JxtaMulticastSocket(peerGroup, pipeAdvertisements.get(i));
                    temp.setSoTimeout(0);
                    multicasts.put(pipeAdvertisements.get(i).getName(),temp);
                    System.out.println("For peer "+pipeAdvertisements.get(i).getName() +" pipe advertisement received.");
                    }
                }
                catch (IOException ex) {
                    Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        public void receive(String senderAtOtherEnd,byte buffer[])
        {
            if(!senderAtOtherEnd.equalsIgnoreCase(AppMainFrame.getUserName())) {
            if(multicasts.containsKey(senderAtOtherEnd+"SocketAdvertisement"))
            {
            
            try {
                DatagramPacket packet=new DatagramPacket(buffer, buffer.length,InetAddress.getLocalHost(),portAddress);
                multicasts.get(senderAtOtherEnd+"SocketAdvertisement").receive(packet);
                 TextMessage msg=new TextMessage(senderAtOtherEnd, new String(packet.getData()));
             //    mediator.sendMessage(ConferenceMediator.RECEIVE_TEXT_MSG, null, msg);
            }
            catch (IOException ex) {
                Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            }
        }

}
