/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.dataobject;

import java.io.Serializable;

/**
 *
 * @author kusu
 */
public class TextMessage implements Serializable {
    private String from;
    private String message;
    private String date;

    public TextMessage() {

    }

    public TextMessage(String from,String msg,String date) {
        this.from=from;
        this.date=date;
        this.message=msg;
    }

    public TextMessage(String sender, String msg) {
       this.from=sender;
       this.message=msg;
    }

    public void setFrom(String frm) {
        this.from=frm;
    }

    public void setMessage(String msg) {
        this.message=msg;
    }

    public void setDate(String date) {
        this.date=date;

    }

    public String getForm() {
        return from;
    }
    public String getMessage() {
        return message;
    }

    public String getDate() {
        return this.date;
    }

}
