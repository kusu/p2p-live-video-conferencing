/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.socket.JxtaMulticastSocket;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
/**
 *
 * @author kusu
 */
public class MulticastClient {
    private JxtaMulticastSocket multicast=null;
    private PeerGroup peerGroup=null;
    private PipeAdvertisement pipeAdvertisement=null;
    private MulticastSocketService multicastSS=null;

    public MulticastClient(PeerGroup peerGroup)
    {
        multicastSS=new MulticastSocketService(peerGroup);
        this.peerGroup=peerGroup;
        pipeAdvertisement=multicastSS.getSocketAdvertisement();
        try {
            multicast = new JxtaMulticastSocket(peerGroup, pipeAdvertisement);
        } catch (IOException ex) {
            Logger.getLogger(MulticastClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMesssage(byte msg[])
    {
        //SocketAddress add=multicast.getRemoteSocketAddress();
        try {
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            multicast.send(packet);

        }
        catch (SocketException ex) {
            Logger.getLogger(MulticastClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
                Logger.getLogger(MulticastClient.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

}
