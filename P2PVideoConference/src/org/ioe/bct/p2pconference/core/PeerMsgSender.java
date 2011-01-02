package org.ioe.bct.p2pconference.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import net.jxta.pipe.OutputPipe;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.ModuleSpecAdvertisement;


/**
 * This tutorial illustrates the use of JXTA Pipes to exchange messages.
 * <p/>
 * This peer is the pipe "server". It opens the pipe for input and waits for
 * messages to be sent. Whenever a Message is received from a "client" the
 * contents are printed.
 */
public class PeerMsgSender {
    private  PeerGroup 		netPeerGroup = null;
    private DiscoveryService 	myDiscoveryService = null;
    private PipeService 	myPipeService = null;
    private PipeAdvertisement myPipeAdvertisement = null;
    private OutputPipe 		myOutputPipe;
   private PipeAdvertisement pipeAdv;
    private String valueString = "JXTA-CH15EX2";
    private final Object lock=new String("lock");
    public PeerMsgSender(P2PNetworkCore netCore)
    {
        netPeerGroup=netCore.getNetPeerGroup();
        getServices();
    }
     private void getServices() {
      
      myDiscoveryService = netPeerGroup.getDiscoveryService();
      myPipeService = netPeerGroup.getPipeService();
    }
    private PipeAdvertisement createPipeAdvertisement(String sender,String receiver) {
        pipeAdv=(PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        pipeAdv.setName(sender+receiver+"pipe");
        pipeAdv.setPipeID(IDFactory.newPipeID(netPeerGroup.getPeerGroupID()));
        pipeAdv.setType(PipeService.UnicastType);
//        try {
//            //createOutputPipe(pipeAdv);
//            myDiscoveryService.publish(pipeAdv);
//        } catch (IOException ex) {
//            Logger.getLogger(PeerMsgSender.class.getName()).log(Level.SEVERE, null, ex);
//        }
        myDiscoveryService.remotePublish(pipeAdv);
        return pipeAdv;
    }
    
    public OutputPipe createOutputPipe(String sender,String receiver) {
      myPipeAdvertisement=createPipeAdvertisement(sender, receiver);
      boolean noPipe = true;
      int count = 0;
      System.out.println(myPipeAdvertisement);
      myOutputPipe = null;
      while (noPipe && count < 10) {
        count++;
        try {
          myOutputPipe = myPipeService.createOutputPipe(myPipeAdvertisement, 60000);
          noPipe = false;
          System.out.println("Output Pipe successfully created");
          Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to create output pipe");
           // System.exit(-1);
        }
      }

      if (count >= 10) {
        System.out.println("no Pipe");
        System.exit(-1);
      }
      return myOutputPipe;
    }


     public void sendData(String data,OutputPipe outputPipe) {
      

    Message msg=new Message();
    StringMessageElement sme=new StringMessageElement("DataMsg", data, null);
    msg.addMessageElement(null, sme);
      try {
        outputPipe.send (msg);
      } catch (Exception e) {
          System.out.println("Unable to print output pipe");
          e.printStackTrace();
          System.exit(-1);
      }
    }
}
