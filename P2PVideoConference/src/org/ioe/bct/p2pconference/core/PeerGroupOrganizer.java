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
public interface PeerGroupOrganizer {
    public ProtectedPeerGroup createPeerGroup(String name,String password, String loginName) throws Exception;
    public void removePeerGroup(ProtectedPeerGroup p);
    public ArrayList<ProtectedPeerGroup> getAllPeerGroups();
    public void updateAllPeerGroups(ArrayList<ProtectedPeerGroup> updatedPGs);
    public PeerGroupService getPeerGroupService();
}
