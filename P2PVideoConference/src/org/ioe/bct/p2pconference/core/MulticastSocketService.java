/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import net.jxta.socket.JxtaMulticastSocket;

/**
 *
 * @author kusu
 */
public class MulticastSocketService {
    private JxtaMulticastSocket multicast=null;

    public MulticastSocketService()
    {
        multicast=new JxtaMulticastSocket();
    }


}
