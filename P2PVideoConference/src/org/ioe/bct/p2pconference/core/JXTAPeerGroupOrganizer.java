/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.util.ArrayList;
import net.jxta.peergroup.PeerGroup;

import org.ioe.bct.p2pconference.dataobject.ProtectedPeerGroup;
import org.ioe.bct.p2pconference.ui.AppMainFrame;
import org.ioe.bct.p2pconference.ui.controls.GroupListener;

/**
 *
 * @author kusum
 */
public class JXTAPeerGroupOrganizer implements PeerGroupOrganizer {

    private ArrayList<ProtectedPeerGroup>  peerGroups =new ArrayList<ProtectedPeerGroup>();
    private GroupDiscoveryThread discoverer;
    private GroupPublishThread publisher;
   
    private GroupListener listener;
    private PeerGroupService service=new PeerGroupService();
    
    public JXTAPeerGroupOrganizer(GroupListener listner){
//        peerGroups=pg;
       this.listener=listner;
      
    }

    public GroupDiscoveryThread getGroupDiscoveryThread() {
        return discoverer;
    }

   public GroupPublishThread getGroupPublishThread() {
       return publisher;
   }

    public ProtectedPeerGroup createPeerGroup(String name, String password,String loginName) throws Exception{
      
        PeerGroup newPeerGroup=service.createPeerGroup(AppMainFrame.netCOre.getNetPeerGroup(), name, loginName, password);
        ProtectedPeerGroup newProtectedPG=new ProtectedPeerGroup(name, password,loginName, newPeerGroup);
        peerGroups.add(newProtectedPG);
        System.out.println("Creating Peer Group...");
        return newProtectedPG;
    }

    public PeerGroupService getPeerGroupService()
    {
        return service;
    }
    public ArrayList<ProtectedPeerGroup> getAllPeerGroups() {
        return peerGroups;
    }

    public void removePeerGroup(ProtectedPeerGroup p) {
        peerGroups.remove(p);
        listener.updatePeerGroups(peerGroups);
    }

    public void updateAllPeerGroups(ArrayList<ProtectedPeerGroup> updatedPGs) {
        peerGroups=updatedPGs;
        listener.updatePeerGroups(updatedPGs);
    }

    public final JXTAPeerGroupOrganizer startThread() {
         discoverer=new GroupDiscoveryThread(this);
       publisher=new GroupPublishThread(this);
     
        publisher.start();
        discoverer.start();
        return this;
    }

   synchronized  public void slowPublishAndDiscovery() {
        publisher.setSleepTimer(25000);
        discoverer.setSleepTimer(45000);
        publisher.lowerPriority();
        discoverer.lowerPriority();
    }

   synchronized  public void rapidPublishAndDiscovery() {
          publisher.setSleepTimer(1000);
          discoverer.setSleepTimer(1000);
          publisher.raisePriority();
          discoverer.raisePriority();
    }

}
