package org.ioe.bct.p2pconference.core;

import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeService;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredTextDocument;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.ModuleClassID;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import org.ioe.bct.p2pconference.patterns.mediator.Mediator;
import org.ioe.bct.p2pconference.ui.controls.ConferenceMediator;

public class PeerMsgReceiver {

    private PeerGroup netPeerGroup;
    private DiscoveryService myDiscoveryService = null;
    private PipeService myPipeService = null;
    private ModuleClassID myService1ID = null;
    private InputPipe myPipe = null;
    private Mediator confMediator;

    public PeerMsgReceiver(P2PNetworkCore netcore,Mediator m) {
        netPeerGroup=netcore.getNetPeerGroup();
        getServices();
        confMediator=m;
    }

     private  void getServices() {
         
      myDiscoveryService =netPeerGroup.getDiscoveryService();
      myPipeService = netPeerGroup.getPipeService();
    }
     
 public void buildModuleAdvertisement() {
	 ModuleClassAdvertisement myService1ModuleAdvertisement = (ModuleClassAdvertisement) AdvertisementFactory.newAdvertisement(ModuleClassAdvertisement.getAdvertisementType());

	 myService1ModuleAdvertisement.setName("P2P");
	 myService1ModuleAdvertisement.setDescription("receiveing data on");

       myService1ID = IDFactory.newModuleClassID();
	 myService1ModuleAdvertisement.setModuleClassID(myService1ID);


       try {
  	   myDiscoveryService.publish(myService1ModuleAdvertisement);
	   myDiscoveryService.remotePublish(myService1ModuleAdvertisement);
       } catch (Exception e) {
         System.out.println("Error during publish of Module Advertisement");
         System.exit(-1);
       }
    }
      public PipeAdvertisement createPipeAdvertisement(String sender,String receiver) {
        PipeAdvertisement pipeAdv=(PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        pipeAdv.setName(sender+receiver+"pipe");
        pipeAdv.setPipeID(IDFactory.newPipeID(netPeerGroup.getPeerGroupID()));
        pipeAdv.setType(PipeService.UnicastType);
           return pipeAdv;
    }

       public void buildModuleSpecificationAdvertisement(PipeAdvertisement myPipeAdvertisement,String name) {

//	StructuredTextDocument paramDoc = (StructuredTextDocument)StructuredDocumentFactory.newStructuredDocument(new MimeMediaType("text/xml"),"Parm");
//	StructuredDocumentUtils.copyElements(paramDoc, paramDoc, (Element)myPipeAdvertisement.getDocument(new MimeMediaType("text/xml")));

	ModuleSpecAdvertisement myModuleSpecAdvertisement = (ModuleSpecAdvertisement) AdvertisementFactory.newAdvertisement(ModuleSpecAdvertisement.getAdvertisementType());

	myModuleSpecAdvertisement.setName(name);
	myModuleSpecAdvertisement.setVersion("Version 1.0");
	myModuleSpecAdvertisement.setCreator("p2pvideoconference");
	myModuleSpecAdvertisement.setModuleSpecID(IDFactory.newModuleSpecID(myService1ID));
	myModuleSpecAdvertisement.setSpecURI("www.ioe.edu.np");
//      myModuleSpecAdvertisement.setParam((StructuredDocument) paramDoc);
      myModuleSpecAdvertisement.setPipeAdvertisement(myPipeAdvertisement);


      try {
        myDiscoveryService.publish(myModuleSpecAdvertisement);
        myDiscoveryService.remotePublish(myModuleSpecAdvertisement);
      } catch (Exception e) {
         System.out.println("Error during publish of Module Specification Advertisement");
         e.printStackTrace();
         System.exit(-1);
      }

      createInputPipe(myPipeAdvertisement);
    }
    public void createInputPipe(PipeAdvertisement myPipeAdvertisement) {
      

      class ServiceListener implements PipeMsgListener{

            public void pipeMsgEvent(PipeMsgEvent pme) {
                Message msg;
                msg=pme.getMessage();
//                JOptionPane.showMessageDialog(null, msg.toString());
               MessageElement msgElement=msg.getMessageElement(null,"DataMsg");

//                JOptionPane.showMessageDialog(null, msgElement.toString());
                confMediator.sendMessage(ConferenceMediator.RECEIVE_TEXT_MSG, null, msgElement.toString());
                
            }

      }
      ServiceListener myService1Listener=new ServiceListener();
      try {
        myPipe = myPipeService.createInputPipe(myPipeAdvertisement, myService1Listener);
      }
      catch (Exception e) {
          System.out.println("Error creating Input Pipe");
          e.printStackTrace();
          System.exit(-1);
      }
    }
    
}