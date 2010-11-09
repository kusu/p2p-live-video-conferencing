/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ioe.bct.p2pconference.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author kusu
 */
public class LoginDialog extends JDialog {

    private JPanel base;
    private Frame owner;
//    private BufferedImage image;



    public LoginDialog(Frame owner, String title) {
        super(owner, title);
        base = new LoginBasePanel();
//        loadImage();
        this.owner=owner;
    }

    public LoginDialog init() {
        this.setLayout(new BorderLayout());
        this.add(base, BorderLayout.CENTER);
        this.setBounds(350, 200, 300, 300);
        addWindowListener(new WindowAdapter() {

           

           

         

            @Override
            public void windowClosing(WindowEvent ee) {
                System.exit(0);
            }

        });
        pack();
        return this;
    }

  
}
