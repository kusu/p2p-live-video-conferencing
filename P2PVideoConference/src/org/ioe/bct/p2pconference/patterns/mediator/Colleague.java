/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.patterns.mediator;

/**
 *
 * @author kusum
 */
public interface Colleague {
//    private Mediator mediator;

    public void receive(String message, Colleague sender,Object body);
    public void setMediator(Mediator m);

}
