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
    public ArrayList getConnectedUsers() {return connectedUsers;}
    public void setConnectUsers(ArrayList users) {this.connectedUsers=users;}
    public void addConnectedUser(Object dt){
        connectedUsers.add(dt);
    }

}
