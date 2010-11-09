/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.mvc.model;

import org.ioe.bct.p2pconference.dataobject.UserInfo;
import org.puremvc.java.interfaces.IProxy;
import org.puremvc.java.patterns.proxy.Proxy;

/**
 *
 * @author kusu
 */
public class LoginProxy extends Proxy implements IProxy {

    private Object userInfo;
    public static final String NAME="LoginProxy";
    
    public LoginProxy(UserInfo uinfo){
        super(NAME, uinfo);
        userInfo=uinfo;
    }
    
    @Override
    public UserInfo getData(){
        return (UserInfo) userInfo;
    }

    @Override
    public void setData(Object pdata){
       userInfo=pdata;
      
    }
}
