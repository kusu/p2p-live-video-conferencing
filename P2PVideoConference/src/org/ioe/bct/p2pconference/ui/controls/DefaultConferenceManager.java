/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui.controls;

import org.ioe.bct.p2pconference.ui.controls.ConferenceMode;
import org.ioe.bct.p2pconference.ui.controls.ConferenceManager;
import org.ioe.bct.p2pconference.patterns.observer.Observer;
import org.ioe.bct.p2pconference.patterns.observer.Subject;
import org.ioe.bct.p2pconference.utils.Notification;

/**
 *
 * @author kusu
 */
public class DefaultConferenceManager implements Subject, ConferenceManager {

    public void registerObserver(Observer o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeObserver(Observer o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void notifyObservers(Notification notificationName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void startConference(ConferenceMode mode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void endConference() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void joinConference() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
