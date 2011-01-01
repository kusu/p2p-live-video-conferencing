/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui.controls;

import java.util.ArrayList;
import org.ioe.bct.p2pconference.patterns.mediator.Colleague;
import org.ioe.bct.p2pconference.patterns.mediator.Mediator;

/**
 *
 * @author kusum
 */
public class ConferenceMediator implements Mediator {

    public static final String CONT_SELECTION_CHANGED="contactselectchanged";
    public static final String GROUP_ADDED="group added";
    public static final String JOIN_GROUP="join group request";
    private ArrayList<Colleague> colleagueList;
    
    public ConferenceMediator(){
        colleagueList=new ArrayList<Colleague>();
    }
    
    public void sendMessage(String message, Colleague originator, Object body) {
        for(Colleague coll: colleagueList) {
            if(coll!=originator ) {
                coll.receive(message, originator,body);
            }
        }
    }

    public void addColleague(Colleague coll) {
        colleagueList.add(coll);
    }

    public void removeColleague(Colleague coll) {
        colleagueList.remove(coll);
    }

}
