package org.ioe.bct.p2pconference.core;

import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.Message;
import net.jxta.peergroup.PeerGroup;
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
public class PeerMsgReceiver {
    private  PeerGroup 		netPeerGroup = null;
    private DiscoveryService 	myDiscoveryService = null;
    private PipeService 	myPipeService = null;
    private PipeAdvertisement myPipeAdvertisement = null;
    private OutputPipe 		myOutputPipe;
   
    private String valueString = "JXTA-CH15EX2";

     private void getServices() {
      
      myDiscoveryService = netPeerGroup.getDiscoveryService();
      myPipeService = netPeerGroup.getPipeService();
    }

     private void findAdvertisement(String searchKey, String searchValue) {
      Enumeration myLocalEnum = null;
     

      try {
        myLocalEnum = myDiscoveryService.getLocalAdvertisements(DiscoveryService.ADV, searchKey, searchValue);

        if ((myLocalEnum != null) && myLocalEnum.hasMoreElements()) {
          
          ModuleSpecAdvertisement myModuleSpecAdv = (ModuleSpecAdvertisement)myLocalEnum.nextElement();

	    myPipeAdvertisement = myModuleSpecAdv.getPipeAdvertisement();
          createOutputPipe(myPipeAdvertisement);
        }
        else {
          DiscoveryListener myDiscoveryListener = new DiscoveryListener() {
            public void discoveryEvent(DiscoveryEvent e) {
              Enumeration enumm;
              PipeAdvertisement pipeAdv = null;
              String str;

           
              DiscoveryResponseMsg myMessage = e.getResponse();
              enumm = myMessage.getResponses();
              str = (String)enumm.nextElement();

              try {
                ModuleSpecAdvertisement myModSpecAdv = (ModuleSpecAdvertisement) AdvertisementFactory.newAdvertisement(MimeMediaType.XMLUTF8,new ByteArrayInputStream(str.getBytes()));
                myPipeAdvertisement = myModSpecAdv.getPipeAdvertisement();

                createOutputPipe(myPipeAdvertisement);
              } catch(Exception ee) {
                  ee.printStackTrace();
                  System.exit(-1);
              }
           }
          };

         
          myDiscoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, searchKey, searchValue, 1, myDiscoveryListener);
        }
      } catch (Exception e) {
          System.out.println("Error during advertisement search");
          System.exit(-1);
      }
    }


    private void createOutputPipe(PipeAdvertisement myPipeAdvertisement) {
      boolean noPipe = true;
      int count = 0;

      myOutputPipe = null;
      while (noPipe && count < 10) {
        count++;
        try {
          myOutputPipe = myPipeService.createOutputPipe(myPipeAdvertisement, 100000);
         
          noPipe = false;
        } catch (Exception e) {
            System.out.println("Unable to create output pipe");
            System.exit(-1);
        }
      }

      if (count >= 10) {
        System.out.println("no Pipe");
        System.exit(-1);
      }
    }


     private void sendData() {
      String data = "Hello my friend!";

//	Message msg = myPipeService.createMessage();
     
      try {
        myOutputPipe.send (msg);
      } catch (Exception e) {
          System.out.println("Unable to print output pipe");
          e.printStackTrace();
          System.exit(-1);
      }
    }
}
