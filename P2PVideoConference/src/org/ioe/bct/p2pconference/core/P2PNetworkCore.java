/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
/**
 *
 * @author kusu
 */
public class P2PNetworkCore {
    private NetworkManager NManager=null;

    private String nodeName="";
    public P2PNetworkCore(String name)
    {
        nodeName=name;
    }
    

    public void setNodeName(String name)
    {
        nodeName=name;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void startNetwork(NetworkManager.ConfigMode modeConfig)
    {
        try{
            NManager=new NetworkManager(modeConfig, nodeName);
            Thread.sleep(2000);
            NManager.startNetwork();
//            System.out.println("Starting Network...");
        }

      
        catch (Exception ge){
            System.out.println("Error "+ge.getMessage());
        }
    }
    public NetworkManager getNetworkManager()
    {
        return NManager;
    }
    public void stopNetwork()
    {
        NManager.stopNetwork();
    }


    public PeerGroup getNetPeerGroup() {
        return NManager.getNetPeerGroup();
    }
  
}
