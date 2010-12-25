/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.ui;

/**
 *
 * @author kusu
 */
public class ConferenceMode {

    private int mode;
    private boolean audioOnly;



    public ConferenceMode(int mode, boolean audioOnly) {
        this.mode=mode;
        this.audioOnly=audioOnly;
    }

    public int getMode(){return  mode;}
    public void setMode(int mode){this.mode=mode;}
    public  boolean isAudioOnly() {return audioOnly;}
    public void setAudioOnly() {this.audioOnly=true;}
    
    public static final int SINGLE=3;
    public static final int GROUP=4;
    public static final int AUDIOONLY=5;

}
