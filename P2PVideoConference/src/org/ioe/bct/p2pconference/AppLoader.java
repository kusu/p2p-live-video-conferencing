/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference;

import org.ioe.bct.p2pconference.ui.AppMainFrame;




/**
 *
 * @author kusu
 */

public final class AppLoader {
   
    public static AppMainFrame mainFrame;

    public static void main(String [] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        }
//        catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Cannot set Look And feel","Error",JOptionPane.ERROR_MESSAGE);
//        }

        mainFrame=new AppMainFrame();
        mainFrame.loadLoginBox();
    }
    

}
