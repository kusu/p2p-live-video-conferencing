 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import org.ioe.bct.p2pconference.ui.AppMainFrame;

/**
 *
 * @author Administrator
 */
public class GroupPublishThread implements Runnable {
    PeerGroupService peerGroupService=null;
    Thread currentThread;
    public GroupPublishThread(PeerGroupOrganizer pGO)
    {
        peerGroupService=pGO.getPeerGroupService();
    }
    @Override
    public void  run()
    {
        while (true) {
        if(peerGroupService!=null)
        peerGroupService.publishAdvertisement(AppMainFrame.netCOre.getNetPeerGroup());
       
        try {
            Thread.sleep(40000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        }
    }

    public void start() {
        currentThread=new Thread(this);
        currentThread.start();
    }
}
