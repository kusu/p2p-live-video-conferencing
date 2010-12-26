/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui.controls;

/**
 *
 * @author kusu
 */
public class ConferenceStatus {
    private int confStatus;

    public int getStatus() {return confStatus;}
    public void setStaus(int status) {this.confStatus=status;}


    public static final int INFO=0;
    public static final int PROCESS=1;
    public static final int STARTED=2;
}
