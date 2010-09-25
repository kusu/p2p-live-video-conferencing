/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.mvc.view;

import org.ioe.bct.p2pconference.ui.LoginDialog;
import org.puremvc.java.interfaces.IMediator;
import org.puremvc.java.patterns.mediator.Mediator;

/**
 *
 * @author kusu
 */
public class LoginDialogMediator extends Mediator implements IMediator {
    
    public static final String MEDNAME="LoginDialogMediator";

    public LoginDialogMediator (LoginDialog loginDialg){
        super(MEDNAME, loginDialg);
        viewComponent=loginDialg;
    }

    @Override
    public LoginDialog getViewComponent(){
        return (LoginDialog) viewComponent;
    }

    @Override
    public void setViewComponent(Object viewComp){
        viewComponent=viewComp;
    }
    
}
