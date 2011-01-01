/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.util.ArrayList;
import java.util.Iterator;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PeerAdvertisement;
import org.ioe.bct.p2pconference.dataobject.ProtectedPeerGroup;
import org.ioe.bct.p2pconference.ui.AppMainFrame;

/**
 *
 * @author kusum
 */
public class GroupDiscoveryThread implements Runnable {
    PeerGroupOrganizer organizer;
    PeerGroupService peerGroupDiscoveryService;
    Thread currentThread;

    public GroupDiscoveryThread(PeerGroupOrganizer org){
        organizer=org;
        peerGroupDiscoveryService=org.getPeerGroupService();
    }

    @Override
    public void run()  {

       while (true) {
          ArrayList<PeerGroup> newPeerGroup= peerGroupDiscoveryService.discoverGroups(AppMainFrame.netCOre.getNetPeerGroup());
          System.out.println("Total Groups: "+newPeerGroup.size());
         ArrayList<ProtectedPeerGroup> newprotectedGroups=organizer.getAllPeerGroups();

         Iterator<PeerGroup> it=newPeerGroup.iterator();
         while(it.hasNext()) {
             PeerGroup current=it.next();
             if(checkForExistence(current, organizer.getAllPeerGroups())) {
                 continue;
             }
            else {
             String peerGroupName=current.getPeerGroupName();
             ProtectedPeerGroup currentProtectedPeerGrp=new ProtectedPeerGroup(peerGroupName,"","",current);
             ArrayList<PeerAdvertisement> pperList=peerGroupDiscoveryService.discoverPeerInGroup(current);
             currentProtectedPeerGrp.setConnectUsers(pperList);
             newprotectedGroups.add(currentProtectedPeerGrp);
             organizer.updateAllPeerGroups(newprotectedGroups);
            
         }
           }
         sleep();
        }
         
    }

    private void sleep() {
        try {
             Thread.sleep(10000);
         }
         catch (InterruptedException e){
             e.printStackTrace();
         }
        }
    

    public void start() {
        currentThread=new Thread(this);
        currentThread.start();
    }

    public boolean checkForExistence(PeerGroup pg, ArrayList<ProtectedPeerGroup> pgList) {
        Iterator<ProtectedPeerGroup> it=pgList.iterator();
        while (it.hasNext()) {
            PeerGroup current=it.next().getPeerGroup();
            if(pg.getPeerGroupID().equals(current.getPeerGroupID())) {
                return true;
            }
        }
        return false;
    }

}
