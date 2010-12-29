/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.util.ArrayList;
import net.jxta.peergroup.PeerGroup;

import org.ioe.bct.p2pconference.dataobject.ProtectedPeerGroup;
import org.ioe.bct.p2pconference.ui.AppMainFrame;

/**
 *
 * @author kusum
 */
public class JXTAPeerGroupOrganizer implements PeerGroupOrganizer {

    private ArrayList<ProtectedPeerGroup>  peerGroups =new ArrayList<ProtectedPeerGroup>();

    private PeerGroupService service=new PeerGroupService();
    public JXTAPeerGroupOrganizer(){}
    public JXTAPeerGroupOrganizer(ArrayList<ProtectedPeerGroup> pg){
        peerGroups=pg;
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
    }

    public void updateAllPeerGroups(ArrayList<ProtectedPeerGroup> updatedPGs) {
        peerGroups=updatedPGs;
    }

}
