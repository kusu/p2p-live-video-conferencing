package org.ioe.bct.p2pconference.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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
      public OutputPipe findAdvertisement(String Peer,String searchKey, String searchValue) {
      Enumeration myLocalEnum = null;
      OutputPipe outputPipe=null;

      try {
        myLocalEnum = myDiscoveryService.getLocalAdvertisements(DiscoveryService.ADV, "Name", "modulespecadd");
        
        if ((myLocalEnum != null) && myLocalEnum.hasMoreElements()) {

          ModuleSpecAdvertisement myModuleSpecAdv = (ModuleSpecAdvertisement)myLocalEnum.nextElement();
          outputPipe=createOutputPipe(myModuleSpecAdv.getPipeAdvertisement());
          JOptionPane.showMessageDialog(null,"FROM LOCAL" + myModuleSpecAdv.getPipeAdvertisement().toString());

        }
        else {
          class ServiceDiscoveryListener implements DiscoveryListener{
            private OutputPipe outputPipe;
              public ServiceDiscoveryListener()
              {
                
            }
            public OutputPipe getOutputPipe()
              {
                return outputPipe;
            }
              public void discoveryEvent(DiscoveryEvent e) {
              Enumeration enumm;
              PipeAdvertisement pipeAdv = null;
              String str;


              DiscoveryResponseMsg myMessage = e.getResponse();
              enumm = myMessage.getResponses();
              str = (String)enumm.nextElement();

              try {
                ModuleSpecAdvertisement myModSpecAdv = (ModuleSpecAdvertisement) AdvertisementFactory.newAdvertisement(MimeMediaType.XMLUTF8,new ByteArrayInputStream(str.getBytes()));
                outputPipe=createOutputPipe(myModSpecAdv.getPipeAdvertisement());
                JOptionPane.showMessageDialog(null, "FROM REMOTE\n"+myModSpecAdv.getPipeAdvertisement().toString());

              } catch(Exception ee) {
                  ee.printStackTrace();
                  System.exit(-1);
              }
           }
          }
          ServiceDiscoveryListener myDiscoveryListener=new ServiceDiscoveryListener();
          outputPipe=myDiscoveryListener.getOutputPipe();
          myDiscoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", "modulespecadd", 1, myDiscoveryListener);
        }
        Thread.sleep(1000);
      } catch (Exception e) {
          System.out.println("Error during advertisement search");
          System.exit(-1);
      }
      if(outputPipe==null) JOptionPane.showMessageDialog(null, "Why am i null");
      return outputPipe;
    }
    public OutputPipe createOutputPipe(PipeAdvertisement myPipeAdvertisement) {
     // myPipeAdvertisement=createPipeAdvertisement(sender, receiver);
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
