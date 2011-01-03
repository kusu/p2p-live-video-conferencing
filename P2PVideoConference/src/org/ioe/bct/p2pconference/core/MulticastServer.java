/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaMulticastSocket;

/**
 *
 * @author kusu
 */
public class MulticastServer {
        private HashMap<String,JxtaMulticastSocket> multicasts=new HashMap<String,JxtaMulticastSocket>();
        private ArrayList<PipeAdvertisement> pipeAdvertisements=new ArrayList<PipeAdvertisement>();
        private MulticastSocketService multicastSS=null;
        private PeerGroup peerGroup=null;

        public MulticastServer(PeerGroup peerGroup)
        {
            this.peerGroup=peerGroup;
            multicastSS=new MulticastSocketService(peerGroup);

        }

        public void getAllMulticastSocketFromPipeAdvertisements()
        {

            multicasts.clear();
            pipeAdvertisements=multicastSS.getAllLocalPipeAdvertisement();
            for(int i=0;i<pipeAdvertisements.size();i++)
            {
                try {
                    JxtaMulticastSocket temp = new JxtaMulticastSocket(peerGroup, pipeAdvertisements.get(i));
                    multicasts.put(pipeAdvertisements.get(i).getName()+"SocketAdvertisement",temp);
                }
                catch (IOException ex) {
                    Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        public void receive(String senderAtOtherEnd,byte buffer[])
        {
            DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
            try {
                multicasts.get(senderAtOtherEnd+"SocketAdvertisement").receive(packet);
            }
            catch (IOException ex) {
                Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

}
