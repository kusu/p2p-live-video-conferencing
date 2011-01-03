/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.protocol.PeerAdvertisement;

/**
 *
 * @author Administrator
 */
public class PeerPublishThread {
    private PeerAdvertisement peerAdv;
    private PeerGroup netPeerGroup;
    private DiscoveryService discoveryService;
    public PeerPublishThread(P2PNetworkCore netCore)
    {
        netPeerGroup=netCore.getNetPeerGroup();
        discoveryService=netPeerGroup.getDiscoveryService();
    }
    public void createAdvertisement(String peerName)
    {
       peerAdv=(PeerAdvertisement)AdvertisementFactory.newAdvertisement(PeerAdvertisement.getAdvertisementType());
       peerAdv.setPeerID(IDFactory.newPeerID(netPeerGroup.getPeerGroupID()));
       peerAdv.setName(peerName);
    }

    public void publishAdvertisement()
    {
        try {
            discoveryService.publish(peerAdv);
            discoveryService.remotePublish(peerAdv, 60*1000*3);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
