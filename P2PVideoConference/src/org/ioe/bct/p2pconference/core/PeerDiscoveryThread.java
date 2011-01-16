/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PeerAdvertisement;

/**
 *
 * @author Administrator
 */
public class PeerDiscoveryThread {

        private PeerGroup peerGroup;
        private DiscoveryService discoveryService;
       public PeerDiscoveryThread(P2PNetworkCore netCore)
       {
            peerGroup=netCore.getNetPeerGroup();
            discoveryService=peerGroup.getDiscoveryService();
            
       }

      private   class ServiceListener implements DiscoveryListener
                {
                    boolean flag=false;
                    PeerAdvertisement discoveredAdv=null;
                    public boolean getflag()
                    {
                        return flag;
                    }

                    public PeerAdvertisement getPeerAdvertisement() {
                        return discoveredAdv;
                    }
                    public void discoveryEvent(DiscoveryEvent event) {
                    Enumeration enumm;
                 
                    String str;
                    DiscoveryResponseMsg myMessage = event.getResponse();
                    enumm = myMessage.getResponses();
                    str = (String)enumm.nextElement();
                    try {
                        discoveredAdv = (PeerAdvertisement) AdvertisementFactory.newAdvertisement(MimeMediaType.XMLUTF8,new ByteArrayInputStream(str.getBytes()));
                        if(discoveredAdv!=null)
                            flag=true;


                      } catch(Exception ee) {
          
                      }
                    }

                }

       public PeerAdvertisement getPeerDescription(String peerName) {
           
           PeerAdvertisement returnValue=null;
           Enumeration peerAds=null;
        try {
            peerAds = discoveryService.getLocalAdvertisements(DiscoveryService.PEER, "Name", peerName);
            if(peerAds!=null)
            {
              returnValue= (PeerAdvertisement)peerAds.nextElement();
            }
            else
            {
               
                ServiceListener myDiscoveryListener=new ServiceListener();
               
                discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, "Name", peerName, 1,myDiscoveryListener);
                return myDiscoveryListener.getPeerAdvertisement();
            }
            return returnValue;

        }
        catch (IOException ex) {
         return null;
         }
       }


       public boolean isAvailable(String searchKey,String searchValue)
       {
           boolean flag=false;
           Enumeration peerAds=null;
        try {
            peerAds = discoveryService.getLocalAdvertisements(DiscoveryService.PEER, searchKey, searchValue);
            if(peerAds!=null)
            {
                flag=true;
            }
            else
            {
               

                ServiceListener myDiscoveryListener=new ServiceListener();
               
                discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, searchKey, searchValue, 1,myDiscoveryListener);
                 flag=myDiscoveryListener.getflag();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
           return flag;
       }
}
