/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.dataobject;

import java.util.ArrayList;
import net.jxta.peergroup.PeerGroup;

/**
 *
 * @author kusum
 */
public class ProtectedPeerGroup {
    private PeerGroup peerGroup;
    private String peerGroupName;
    private String peerGroupPassword;
    private String peerGroupLoginName;
    private ArrayList connectedUsers=new ArrayList();

    public ProtectedPeerGroup(String name,String password,String loginName, PeerGroup pg) {
        peerGroupName=name;
        peerGroupPassword=password;
        peerGroup=pg;
        loginName=peerGroupLoginName;

    }

    public String getPassword() {return peerGroupPassword;}
    public PeerGroup getPeerGroup() {return peerGroup;}
    public String getGroupName() {return peerGroupName;}
    public ArrayList getConnectedUsers() {return connectedUsers;}
    public void addConnectedUser(Object dt){
        connectedUsers.add(dt);
    }

}
