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
    private long sleepTimer=1000;

    public void setSleepTimer(long timer) {
        sleepTimer=timer;
    }

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
            Thread.sleep(sleepTimer);
        } catch (InterruptedException ex) {
           System.out.println("ERROR IN THREADING: "+ex.getMessage());
        }
        }
    }

    public void start() {
        currentThread=new Thread(this);
        currentThread.start();
    }

     public void lowerPriority() {
        currentThread.setPriority(Thread.MIN_PRIORITY);
    }
    public void raisePriority() {
        currentThread.setPriority(Thread.NORM_PRIORITY);
    }
}
