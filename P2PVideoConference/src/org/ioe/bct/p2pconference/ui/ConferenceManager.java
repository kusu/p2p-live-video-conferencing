/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui;

/**
 *
 * @author kusu
 */
public interface ConferenceManager {

    public void startConference(ConferenceMode mode);
    public void endConference();
    public void joinConference();

}
