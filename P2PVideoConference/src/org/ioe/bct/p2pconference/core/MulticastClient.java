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
import net.jxta.discovery.DiscoveryService;
import net.jxta.socket.JxtaMulticastSocket;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
/**
 *
 * @author kusu
 */
public class MulticastClient implements Runnable{
    private JxtaMulticastSocket multicast=null;
    private PeerGroup peerGroup=null;
    private PipeAdvertisement pipeAdvertisement=null;
    private MulticastSocketService multicastSS=null;
    private DiscoveryService discoveryService=null;
    public MulticastClient(PeerGroup peerGroup,String creator)
    {
        multicastSS=new MulticastSocketService(peerGroup);
        this.peerGroup=peerGroup;
        pipeAdvertisement=multicastSS.getSocketAdvertisement(creator);
        multicastSS.buildModuleAdvertisement();
        multicastSS.buildModuleSpecificationAdvertisement(pipeAdvertisement);
      //  System.out.println(pipeAdvertisement);
        discoveryService=peerGroup.getDiscoveryService();
        try {
            multicast = new JxtaMulticastSocket(peerGroup, pipeAdvertisement);
            multicast.setSoTimeout(0);
        } catch (IOException ex) {
            Logger.getLogger(MulticastClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void publishPipeAdvertisement()
    {
        multicastSS.publishModuleAdvertisement();
        multicastSS.publishModuleSpecificationAdvertisement();
    }
    public void sendMesssage(String message)
    {
        //SocketAddress add=multicast.getRemoteSocketAddress();
        byte[] msg=message.getBytes();
        
        try {
            
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            multicast.send(packet);
            System.out.println("MESSAGE "+new String(packet.getData())+"  from port : "+packet.getPort());
        }
        catch (SocketException ex) {
            Logger.getLogger(MulticastClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
                Logger.getLogger(MulticastClient.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

    public void run() {
       // try {
        
        publishPipeAdvertisement();
        
        
//            Thread.sleep(20000);
//        } catch (InterruptedException ex) {
//            System.out.println(ex.getMessage());
//        }
    }

}
