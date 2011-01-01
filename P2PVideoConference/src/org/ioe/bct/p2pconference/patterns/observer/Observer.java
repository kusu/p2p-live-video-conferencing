/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.patterns.observer;

import org.ioe.bct.p2pconference.utils.Notification;

/**
 *
 * @author kusu
 */
public interface Observer {

    public void update(Notification type);
}
