/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.dataobject;

import java.util.ArrayList;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PeerAdvertisement;

/**
 *
 * @author kusum
 */
public class ProtectedPeerGroup {
    private PeerGroup peerGroup;
    private String peerGroupName;
    private String peerGroupPassword;
    private String peerGroupLoginName;
    private ArrayList<PeerAdvertisement> connectedUsers=new ArrayList<PeerAdvertisement>();
    private boolean conferenceStarted=false;

    public ProtectedPeerGroup(String name,String password,String loginName, PeerGroup pg) {
        peerGroupName=name;
        peerGroupPassword=password;
        peerGroup=pg;
        peerGroupLoginName=loginName;

    }

    public boolean isConferenceStarted() {return conferenceStarted;}
    public void setConferenceStarted(boolean isStart) {
        conferenceStarted=isStart;
    }

    public String getPassword() {return peerGroupPassword;}
    public PeerGroup getPeerGroup() {return peerGroup;}
    public String getGroupName() {return peerGroupName;}
    public String getGroupLoginName(){ return peerGroupLoginName;}
    public ArrayList<PeerAdvertisement> getConnectedUsers() {return connectedUsers;}
    
    public void setConnectUsers(ArrayList<PeerAdvertisement> users) {this.connectedUsers=users;}
    public void addConnectedUser(PeerAdvertisement dt){
        connectedUsers.add(dt);
    }


}
