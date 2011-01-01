/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.patterns.mediator;

/**
 *
 * @author kusum
 */
public interface Mediator {

    public void sendMessage(String message, Colleague coll, Object body );
    public void addColleague(Colleague coll);
    public void removeColleague(Colleague coll);
}
