/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.mvc;

import org.ioe.bct.p2pconference.AppLoader;
import org.puremvc.java.interfaces.IFacade;
import org.puremvc.java.patterns.facade.Facade;

/**
 *
 * @author kusu
 */
public class ApplicationFacade extends Facade implements IFacade {

   
    public static ApplicationFacade getInstance(){
        if(instance==null){
            instance=new ApplicationFacade();
        }
        ApplicationFacade appInstance=(ApplicationFacade)instance;
        return appInstance;
    }

    @Override
    protected void initializeController(){
        super.initializeController();
    }

    public void startApplication(AppLoader myApp){
       
    }
}
