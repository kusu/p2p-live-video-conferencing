/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.dataobject;

/**
 *
 * @author kusum
 */
public interface PeerResolver {

    public String getName();
    public String getEmail();
    public String getStatus();
    public String getType();
    public String getIP();
    public String getUUID();

}
