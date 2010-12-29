/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.ioe.bct.p2pconference.AppLoader;
import org.ioe.bct.p2pconference.ui.AppMainFrame;

/**
 *
 * @author Administrator
 */
public class GroupPublishThread extends Thread{

    PeerGroupService peerGroupService=null;
    public GroupPublishThread(PeerGroupOrganizer pGO)
    {
        peerGroupService=pGO.getPeerGroupService();
    }
    @Override
    public void run()
    {
        peerGroupService.publishAdvertisement(AppMainFrame.netCOre.getNetPeerGroup());
       
        try {
            Thread.sleep(120000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
    }
}
