/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.dataobject;

import net.jxta.protocol.PeerAdvertisement;
import org.ioe.bct.p2pconference.core.P2PNetworkCore;
import org.ioe.bct.p2pconference.core.PeerDiscoveryThread;

/**
 *
 * @author kusu
 */
public class JXTAPeerResolver implements PeerResolver{
    private String peerName;
   
     PeerDiscoveryThread discoverer;
     PeerAdvertisement peerAdv=null;
    public JXTAPeerResolver(String pper,P2PNetworkCore netCore) {
        peerName=pper;
      
        discoverer=new PeerDiscoveryThread(netCore);
        peerAdv=discoverer.getPeerDescription(peerName);
    }

    public String getName() {
       return peerName;
    }

    public String getEmail() {
        return "N/A";
    }

    public String getStatus() {
      
      
       if(peerAdv!=null) {
           return "Available";
       }else {
           return "Not Available";
       }
    }

    public String getType() {
      if(peerAdv==null) {
            return "N/A";
        }else {
            return peerAdv.getDescription();
        }
    }

    public String getIP() {
        return "133.545.56.1";
    }

    public String getUUID() {

        if(peerAdv==null) {
            return "N/A";
        }else {
            return peerAdv.getID().toString();
        }
    }

}
