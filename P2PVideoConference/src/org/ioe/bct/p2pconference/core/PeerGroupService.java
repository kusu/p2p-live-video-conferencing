/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import net.jxta.discovery.DiscoveryService;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
/**
 *
 * @author Administrator
 */
public class PeerGroupService {
    
    public PeerGroup createPeerGroup(PeerGroup rootPeerGroup,String groupName)
    {
        // - Create the Peer Group by doing the following:
        // - Create a Peer Group Module Implementation Advertisement and publish it
        // - Create a Peer Group Adv and publish it
        // - Create a Peer Group from the Peer Group Adv and return this object
        PeerGroup satellaPeerGroup=null;
        PeerGroupAdvertisement satellaPeerGroupAdvertisement;
        // Create the PeerGroup Module Implementation Adv
        ModuleImplAdvertisement passwdMembershipModuleImplAdv;
        passwdMembershipModuleImplAdv = this.createPasswdMembershipPeerGroupModuleImplAdv(rootPeerGroup);
        // Publish it in the parent peer group
        DiscoveryService rootPeerGroupDiscoveryService =
                rootPeerGroup.getDiscoveryService();
        try {
            rootPeerGroupDiscoveryService.publish(
                    passwdMembershipModuleImplAdv,
                    PeerGroup.DEFAULT_LIFETIME,
                    PeerGroup.DEFAULT_EXPIRATION);
            rootPeerGroupDiscoveryService.remotePublish(
                    passwdMembershipModuleImplAdv,
                    PeerGroup.DEFAULT_EXPIRATION);
        } catch (java.io.IOException e) {
            System.err.println("Can't Publish passwdMembershipModuleImplAdv");
            System.exit(1);
        }
        // Now, Create the Peer Group Advertisement
        satellaPeerGroupAdvertisement =

        this.createPeerGroupAdvertisement(passwdMembershipModuleImplAdv,groupName,login,passwd);
        // Publish it in the parent peer group
    try {
        rootPeerGroupDiscoveryService.publish(
                    satellaPeerGroupAdvertisement,
                    PeerGroup.DEFAULT_LIFETIME,
                    PeerGroup.DEFAULT_EXPIRATION);
                    rootPeerGroupDiscoveryService.remotePublish(
                    satellaPeerGroupAdvertisement,
                    PeerGroup.DEFAULT_EXPIRATION);
    }
    catch (java.io.IOException e) {
        System.err.println("Can't Publish satellaPeerGroupAdvertisement");
        System.exit(1);
        }
    // Finally Create the Peer Group
    if (satellaPeerGroupAdvertisement == null) {
            System.err.println("satellaPeerGroupAdvertisement is null");
    }
    try {
        satellaPeerGroup = rootPeerGroup.newGroup(satellaPeerGroupAdvertisement);
    }
    catch (net.jxta.exception.PeerGroupException e) {
        System.err.println("Can't create Satella Peer Group from Advertisement");
        e.printStackTrace();
        return null;
    }
    return satellaPeerGroup;

    }

    public PeerGroup discoverPeerGroup()
    {
        return null;
    }
    
    public void joinPeerGroup()
    {
        
    }

    //Discovering Peers in the joinedGroup
    //work left
}
