/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.utils;

/**
 *
 * @author kusu
 */
public class Notification {
    private Object body;
    private int name;
    public static final int CONTACT_ADDED=0;
    public static final int CONTACT_DELETED=1;
    public static final int CONTACT_LOADED=4;

    public Notification(){}
    public Notification (int name) {
        this.name=name;
    }

    public void setBody(Object bod) {
        body=bod;
    }
    
    public Object getBody() {
        return body;
    }
    
    public void setName(int name) {
        this.name=name;

    }

    public int getName() {
        return name;
    }

}
