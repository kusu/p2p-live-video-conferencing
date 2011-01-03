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
                class ServiceListener implements DiscoveryListener
                {
                    boolean flag=false;

                    public boolean getflag()
                    {
                        return flag;
                    }

                    public void discoveryEvent(DiscoveryEvent event) {
                    Enumeration enumm;
                    PeerAdvertisement peerAdv = null;
                    String str;
                    DiscoveryResponseMsg myMessage = event.getResponse();
                    enumm = myMessage.getResponses();
                    str = (String)enumm.nextElement();
                    try {
                        PeerAdvertisement myPeerAdv = (PeerAdvertisement) AdvertisementFactory.newAdvertisement(MimeMediaType.XMLUTF8,new ByteArrayInputStream(str.getBytes()));
                        if(myPeerAdv!=null)
                            flag=true;


                      } catch(Exception ee) {
                          ee.printStackTrace();
             //             System.exit(-1);
                      }
                    }

                }

                ServiceListener myDiscoveryListener=new ServiceListener();
                flag=myDiscoveryListener.getflag();
                discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, searchKey, searchValue, 1,myDiscoveryListener);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
           return flag;
       }
}
