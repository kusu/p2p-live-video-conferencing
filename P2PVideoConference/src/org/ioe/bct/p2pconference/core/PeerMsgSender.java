package org.ioe.bct.p2pconference.core;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeService;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredTextDocument;
import net.jxta.endpoint.Message;
import net.jxta.platform.ModuleClassID;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

public class PeerMsgSender {

    private P2PNetworkCore networkCore;
   private DiscoveryService myDiscoveryService = null;
    private PipeService myPipeService = null;
    private ModuleClassID myService1ID = null;
    private InputPipe myPipe = null;


    public PeerMsgSender(P2PNetworkCore netcore) {
        networkCore=netcore;
        getServices();
    }

     private  void getServices() {
         
      myDiscoveryService =networkCore.getNetPeerGroup().getDiscoveryService();
      myPipeService = networkCore.getNetPeerGroup().getPipeService();
    }
     
    public void buildModuleAdvertisement() {
	 ModuleClassAdvertisement myService1ModuleAdvertisement = (ModuleClassAdvertisement) AdvertisementFactory.newAdvertisement(ModuleClassAdvertisement.getAdvertisementType());

	 myService1ModuleAdvertisement.setName("JXTAMOD:JXTA-CH15EX2");
	 myService1ModuleAdvertisement.setDescription("Service 1 of Chapter 15 example 2");

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

    public void buildModuleSpecificationAdvertisement(PipeAdvertisement myPipeAdvertisement) {

//	StructuredTextDocument paramDoc = (StructuredTextDocument)StructuredDocumentFactory.newStructuredDocument(new MimeMediaType("text/xml"),"Parm");
//	StructuredDocumentUtils.copyElements(paramDoc, paramDoc, (Element)myPipeAdvertisement.getDocument(new MimeMediaType("text/xml")));

	ModuleSpecAdvertisement myModuleSpecAdvertisement = (ModuleSpecAdvertisement) AdvertisementFactory.newAdvertisement(ModuleSpecAdvertisement.getAdvertisementType());

	myModuleSpecAdvertisement.setName("JXTASPEC:JXTA-CH15EX2");
	myModuleSpecAdvertisement.setVersion("Version 1.0");
	myModuleSpecAdvertisement.setCreator("gradecki.com");
	myModuleSpecAdvertisement.setModuleSpecID(IDFactory.newModuleSpecID(myService1ID));
	myModuleSpecAdvertisement.setSpecURI("<http://www.jxta.org/CH15EX2>");
//      myModuleSpecAdvertisement.setParam((StructuredDocument) paramDoc);
      myModuleSpecAdvertisement.setPipeAdvertisement(myPipeAdvertisement);

        try {
          StructuredTextDocument doc = (StructuredTextDocument)myModuleSpecAdvertisement.getDocument(new MimeMediaType("text/xml"));
          doc.sendToStream(System.out);
        } catch(Exception e) {}

      
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

    private void createInputPipe(PipeAdvertisement myPipeAdvertisement) {
      

      PipeMsgListener myService1Listener = new PipeMsgListener() {
        public void pipeMsgEvent(PipeMsgEvent event) {
          

            
             }
      };

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