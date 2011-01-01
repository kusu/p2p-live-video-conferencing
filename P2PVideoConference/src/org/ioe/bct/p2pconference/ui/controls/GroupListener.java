/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui.controls;

import java.util.ArrayList;
import org.ioe.bct.p2pconference.dataobject.ProtectedPeerGroup;

/**
 *
 * @author kusu
 */
public interface  GroupListener {

    public void updatePeerGroups(ArrayList<ProtectedPeerGroup> updatedPGs);
}
