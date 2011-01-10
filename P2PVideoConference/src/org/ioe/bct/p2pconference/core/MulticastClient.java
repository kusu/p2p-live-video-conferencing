/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
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
    private int portAddress=9291;  //default value so as not to alter anything in text multicasting already implemented
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
    public MulticastClient(PeerGroup peerGroup,String creator,String Mode)
    {

        multicastSS=new MulticastSocketService(peerGroup);
        multicastSS.setMode(Mode);
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
    public void setMode(String Mode)    //set the mode for either conferenceAudio or conferenceText(default no need to set explicitly)
        {
            multicastSS.setMode(Mode);
        }

        public void setPort(int port)   //set the port for different variation of application(e.g audio and/or text) of this class
        {
            portAddress=port;
        }
    public void publishPipeAdvertisement()
    {
        
        multicastSS.publishModuleAdvertisement();
        multicastSS.publishModuleSpecificationAdvertisement();
      
    }
    public void sendMesssage(byte[] msg)
    {
        //SocketAddress add=multicast.getRemoteSocketAddress();
       // byte[] msg=message.getBytes();
        
        try {
//            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            DatagramPacket packet = new DatagramPacket(msg, msg.length,InetAddress.getLocalHost(),portAddress);
            multicast.send(packet);

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
