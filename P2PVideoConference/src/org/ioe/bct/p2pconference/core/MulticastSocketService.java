/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
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
    public MulticastSocketService(PeerGroup pg)
    {
        peerGroup=pg;
        ds=peerGroup.getDiscoveryService();
    }
    public  PipeAdvertisement getSocketAdvertisement()
    {
        PipeID socketID=null;
        socketID=(PipeID)IDFactory.newPipeID(peerGroup.getPeerGroupID());
        PipeAdvertisement advertisement=(PipeAdvertisement)AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        advertisement.setPipeID(socketID);
        advertisement.setType(PipeService.PropagateType);
        ds.remotePublish(advertisement);
        return advertisement;
    }

    public ArrayList<PipeAdvertisement> getAllLocalPipeAdvertisement()
    {
        ArrayList<PipeAdvertisement> localAdvs=new ArrayList<PipeAdvertisement>();

        try {
            Enumeration<Advertisement> advertisements = ds.getLocalAdvertisements(DiscoveryService.ADV, null, null);
            while(advertisements.hasMoreElements())
            {
                Advertisement adv=advertisements.nextElement();
                if(adv.getAdvType().equals("JxtaPropagate"))
                {
                    if(adv instanceof PipeAdvertisement){
                        localAdvs.add((PipeAdvertisement)adv);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MulticastSocketService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return localAdvs;

    }



}