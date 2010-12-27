/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
/**
 *
 * @author Administrator
 */
public class DiscoveryClient {
    private NetworkManager NManager;
    private DiscoveryService discoveryService;

    public void DiscoveryClient(NetworkManager manager)
    {
        NManager=manager;
    }
}
