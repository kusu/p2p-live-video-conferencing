/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui;

/**
 *
 * @author kusu
 */



public interface ConferenceUIManager {

   

    public void startConference(int mode);
    public void setMode(int mode);
    public int getMode(int mode);
    public void updateUI();
}
