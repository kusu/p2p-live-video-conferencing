/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kusu
 */
public class ConferenceList extends PeerList {

    private Map<String,ArrayList> confList;
    
    public ConferenceList() {
        confList=new HashMap<String, ArrayList>();
    }

}
