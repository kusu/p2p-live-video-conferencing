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

    public static final String AUDIO_REQUEST_CODE="AC-125801-47862-307-SG";
    public static final String CONT_SELECTION_CHANGED="contactselectchanged";
    public static final String GROUP_ADDED="group added";
    public static final String JOIN_GROUP="join group request";
    public static final String PRIVATE_VOICE_CALL_SYNC="Audio call sync";
    public static final String SEND_TEXT_MSG="Send Group MSg";
    public static final String  RECEIVE_TEXT_MSG="ReceiveTxtMsg";
    public static final String PRIVATE_CALL_ACCPTED="ACAC-125801-47862-307-SGpAcA";
    public static final String PRIVATE_CALL_REJECTED="AC-125801-47862-307-SGpACr";
    public static final String PRIVATE_CALL_ACCEPT_MSGQ="Accept the call";
    public static final String PRIVATE_CALL_REJECT_MSGQ="REject the call";
    public static final String PRIVATE_CALL_REJECT_CODE="pcamACAC-125801-47862-307-SGpAcAReject";
     public static final String PRIVATE_CALL_ACCEPT_CODE="pcamACAC-125801-47862-307-SGpAcARejectA";
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
