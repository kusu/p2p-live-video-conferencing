/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.ModuleClassID;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaMulticastSocket;

/**
 *
 * @author kusu
 */
public class MulticastSocketService {
    private JxtaMulticastSocket multicastSocket=null;
    private PeerGroup peerGroup=null;
    private DiscoveryService ds=null;
    private ModuleClassID myService1ID = null;
    private ModuleClassAdvertisement myService1ModuleAdvertisement;
    private ModuleSpecAdvertisement myModuleSpecAdvertisement;
    private String MODE="conferenceTextModuleSpec";   //defaultMode
    public MulticastSocketService(PeerGroup pg)
    {
        peerGroup=pg;
        ds=peerGroup.getDiscoveryService();
    }
    public void setMode(String mode)
    {
        MODE=mode;  //user defined Mode   possibility : conferenceAudioModuleSpec
    }
    public  PipeAdvertisement getSocketAdvertisement(String creator)
    {
        PipeID socketID=null;
        socketID=(PipeID)IDFactory.newPipeID(peerGroup.getPeerGroupID());
        PipeAdvertisement advertisement=(PipeAdvertisement)AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        advertisement.setPipeID(socketID);
        advertisement.setName(creator+"SocketAdvertisement");
        advertisement.setType(PipeService.PropagateType);
        return advertisement;
    }
     public void buildModuleAdvertisement() {
	 myService1ModuleAdvertisement = (ModuleClassAdvertisement) AdvertisementFactory.newAdvertisement(ModuleClassAdvertisement.getAdvertisementType());

	 myService1ModuleAdvertisement.setName("Multicasting");
	 myService1ModuleAdvertisement.setDescription("receiveing data on");

       myService1ID = IDFactory.newModuleClassID();
	 myService1ModuleAdvertisement.setModuleClassID(myService1ID);


       try {
  	   ds.publish(myService1ModuleAdvertisement);
	   ds.remotePublish(myService1ModuleAdvertisement);
       } catch (Exception e) {
         System.out.println("Error during publish of Module Advertisement");
         System.exit(-1);
       }
    }
     public void publishModuleAdvertisement()
    {
         try {
  	   ds.publish(myService1ModuleAdvertisement);
	   ds.remotePublish(myService1ModuleAdvertisement);
       } catch (Exception e) {
         System.out.println("Error during publish of Module Advertisement");
         System.exit(-1);
       }
     }
     public void buildModuleSpecificationAdvertisement(PipeAdvertisement myPipeAdvertisement) {

//	StructuredTextDocument paramDoc = (StructuredTextDocument)StructuredDocumentFactory.newStructuredDocument(new MimeMediaType("text/xml"),"Parm");
//	StructuredDocumentUtils.copyElements(paramDoc, paramDoc, (Element)myPipeAdvertisement.getDocument(new MimeMediaType("text/xml")));


         myModuleSpecAdvertisement = (ModuleSpecAdvertisement) AdvertisementFactory.newAdvertisement(ModuleSpecAdvertisement.getAdvertisementType());

	myModuleSpecAdvertisement.setName(peerGroup.getPeerGroupName()+MODE);
	myModuleSpecAdvertisement.setVersion("Version 1.0");
	myModuleSpecAdvertisement.setCreator("p2pvideoconference");
	myModuleSpecAdvertisement.setModuleSpecID(IDFactory.newModuleSpecID(myService1ID));
	myModuleSpecAdvertisement.setSpecURI("www.ioe.edu.np");
//      myModuleSpecAdvertisement.setParam((StructuredDocument) paramDoc);
      myModuleSpecAdvertisement.setPipeAdvertisement(myPipeAdvertisement);

      JOptionPane.showMessageDialog(null, myModuleSpecAdvertisement);
      try {
        ds.publish(myModuleSpecAdvertisement);
        ds.remotePublish(myModuleSpecAdvertisement);
      } catch (Exception e) {
         System.out.println("Error during publish of Module Specification Advertisement");
         e.printStackTrace();
         System.exit(-1);
      }
    }
    public void publishModuleSpecificationAdvertisement()
    {
        try {
        ds.publish(myModuleSpecAdvertisement);
        ds.remotePublish(myModuleSpecAdvertisement);
      } catch (Exception e) {
         System.out.println("Error during publish of Module Specification Advertisement");
         e.printStackTrace();
         System.exit(-1);
      }

    }
    public ArrayList<PipeAdvertisement> getAllLocalPipeAdvertisement()
    {
        ArrayList<PipeAdvertisement> localAdvs=new ArrayList<PipeAdvertisement>();

        try {
            Enumeration<Advertisement> advertisements = ds.getLocalAdvertisements(DiscoveryService.ADV, "Name", peerGroup.getPeerGroupName()+"textmodulespec");
                while(advertisements.hasMoreElements())
                {
                    ModuleSpecAdvertisement myModuleSpecAdv = (ModuleSpecAdvertisement)advertisements.nextElement();
                    localAdvs.add(myModuleSpecAdv.getPipeAdvertisement());
                }
            
            
                class ServiceListener implements DiscoveryListener{
                    private ArrayList<PipeAdvertisement> localAds=new ArrayList<PipeAdvertisement>();
                    public ArrayList<PipeAdvertisement> getAdvertisements()
                    {
                        return localAds;
                    }
                    public void discoveryEvent(DiscoveryEvent event) {
                        System.out.println("Am i ever here");
                        Enumeration enumm;
                        PipeAdvertisement pipeAdv = null;
                        String str;
                        DiscoveryResponseMsg myMessage = event.getResponse();
                        enumm = myMessage.getResponses();
                        while(enumm.hasMoreElements())
                        {
                            str = (String)enumm.nextElement();
                        try {
                            ModuleSpecAdvertisement myModSpecAdv = (ModuleSpecAdvertisement) AdvertisementFactory.newAdvertisement(MimeMediaType.XMLUTF8,new ByteArrayInputStream(str.getBytes()));
                            localAds.add(myModSpecAdv.getPipeAdvertisement());
                            
                      } catch(Exception ee) {
                          ee.printStackTrace();
             //             System.exit(-1);
                      }
                        }
                    }

                }
                ServiceListener myDiscoveryListener=new ServiceListener();
                localAdvs.addAll(myDiscoveryListener.getAdvertisements());
                ds.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", peerGroup.getPeerGroupName()+MODE, 10,myDiscoveryListener);
            
        } catch (IOException ex) {
            Logger.getLogger(MulticastSocketService.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Total collected SocketPipeAdvertisement " + localAdvs.size());
        return localAdvs;

    }



}
