/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.util.ArrayList;

import org.ioe.bct.p2pconference.dataobject.ProtectedPeerGroup;

/**
 *
 * @author kusum
 */
public class JXTAPeerGroupOrganizer implements PeerGroupOrganizer {

    private ArrayList<ProtectedPeerGroup>  peerGroups =new ArrayList<ProtectedPeerGroup>();

  
    public ProtectedPeerGroup createPeerGroup(String name, String password,String loginName) {
        ProtectedPeerGroup newProtectedPG=new ProtectedPeerGroup(name, password,loginName, null);
        peerGroups.add(newProtectedPG);
        System.out.println("Creating Peer Group...");
        return newProtectedPG;
    }

    public ArrayList getAllPeerGroups() {
        return peerGroups;
    }

    public void removePeerGroup(ProtectedPeerGroup p) {
        peerGroups.remove(p);
    }

}
