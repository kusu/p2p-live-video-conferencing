/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.dataobject;

/**
 *
 * @author kusu
 */
public class Request {

    private String to;
    private String message;
    
    public Request(){
        to="";
        message="Please add me on your contacts";
    }
    
    public Request(String to) {
        this.to=to;
    }
    public Request(String to, String message) {
        this.to=to;
        this.message=message;
                
    }

    public String getMessage() {
        return message;
    }

    public String getTo() {
        return to;
    }

}
