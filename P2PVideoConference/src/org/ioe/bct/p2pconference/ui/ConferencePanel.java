/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ConferencePanel.java
 *
 * Created on Dec 24, 2010, 4:12:47 PM
 */

package org.ioe.bct.p2pconference.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.ioe.bct.p2pconference.ui.controls.ConferenceMediator;
import org.ioe.bct.p2pconference.ui.controls.ConferenceManager;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PeerAdvertisement;
import org.ioe.bct.p2pconference.core.MulticastClient;
import org.ioe.bct.p2pconference.core.MulticastServer;
import org.ioe.bct.p2pconference.core.P2PNetworkCore;
import org.ioe.bct.p2pconference.core.PrivateMsgManager;

import org.ioe.bct.p2pconference.dataobject.PeerResolver;
import org.ioe.bct.p2pconference.dataobject.ProtectedPeerGroup;
import org.ioe.bct.p2pconference.dataobject.TextMessage;
import org.ioe.bct.p2pconference.patterns.mediator.Colleague;
import org.ioe.bct.p2pconference.patterns.mediator.Mediator;





/**
 *
 * @author kusu
 */
public class ConferencePanel extends javax.swing.JPanel implements  Colleague {

    private Mediator confMediator;
    private PrivateMsgManager privateMsgManager;
    private HashMap<PeerGroup,MulticastClient> multicastClients;
    private ArrayList<MulticastToPeerGroupMapper> mapperArrayList= new ArrayList<MulticastToPeerGroupMapper>();
    private Thread[] serverThreads;
    private Thread clientThread;
    private boolean privateMode=true;
    private ProtectedPeerGroup currentGroup;
    public void setPrivateMode(boolean mode) {
        privateMode=mode;
    }

    private class MulticastToPeerGroupMapper
    {
        private ProtectedPeerGroup pPeerGroup;
        private MulticastServer multicastServer;
        public MulticastToPeerGroupMapper(ProtectedPeerGroup pg,MulticastServer multiS)
        {
            pPeerGroup=pg;
            multicastServer=multiS;
        }

        public MulticastServer getMulticastServer()
        {
            return multicastServer;
        }
        public ProtectedPeerGroup getProtectedPeerGroup()
        {
            return pPeerGroup;
        }
    }


    /** Creates new form ConferencePanel */
    public ConferencePanel() {
        initComponents();
        upperPanel.setVisible(false);
        jScrollPane1.setVisible(false);
        multicastClients=new HashMap<PeerGroup, MulticastClient>();
       
    }

    public void initiateChatting(P2PNetworkCore netCore)
    {
        privateMsgManager=new PrivateMsgManager(AppMainFrame.getUserName(),netCore , confMediator);
    }
    public void setMediator(Mediator m) {
        this.confMediator=m;
        confMediator.addColleague(this);
    }
    private class MulticastServerThread implements Runnable{

        private MulticastServer server;
        private ArrayList<PeerAdvertisement> peerAdvList;
        private String message;
        public MulticastServerThread(MulticastServer multicastServer,ArrayList<PeerAdvertisement> peerAdvs)
        {
            this.server=multicastServer;
            this.peerAdvList=peerAdvs;
        }
        public void run() {
            while(true)
            {
                
                    Iterator<PeerAdvertisement> it=peerAdvList.iterator();
                    while(it.hasNext())
                    {
                        PeerAdvertisement peerAdv=it.next();
                        byte[] buffer = new byte[50];
                        server.getAllMulticastSocketFromPipeAdvertisements();
                        server.receive(peerAdv.getName(), buffer);
                        message = new String(buffer);
                        printMessage(peerAdv.getName(), message);
                    }
                   
                    sleep(10000);
            }
        }

        
    }
    private void sleep (long timeout) {
        try {
             Thread.sleep(timeout);
        }
        catch (InterruptedException ex) {
                   System.out.println(ex.getMessage());
        }
    }

    private class MulticastClientThread implements Runnable{
        Collection<MulticastClient> multicastClients;
        public MulticastClientThread(Collection<MulticastClient> mcastClients)
        {
            this.multicastClients=mcastClients;
        }

        public void run() {
            while(true)
            {
                
                    Iterator<MulticastClient> it = multicastClients.iterator();
                    while (it.hasNext()) {
                        MulticastClient mcastClient = it.next();
                        mcastClient.publishPipeAdvertisement();
                    
                   
                }
                   sleep(10000);
            }
        }
        
    }
    public void startAllMulticastClientThread()
    {
        clientThread=new Thread(new MulticastClientThread(multicastClients.values()));
        System.out.println("Execution Client Thread :");
        clientThread.start();
     }
    public void startAllMulticastServerThread()
    {
        Iterator<MulticastToPeerGroupMapper> itr=mapperArrayList.iterator();
        serverThreads=new Thread[mapperArrayList.size()];
        int i=0;
        while(itr.hasNext())
        {
            MulticastToPeerGroupMapper current=itr.next();
            ProtectedPeerGroup peerGroup=current.getProtectedPeerGroup();
            ArrayList<PeerAdvertisement> peerAdvList=peerGroup.getConnectedUsers();
            Iterator<PeerAdvertisement> it=peerAdvList.iterator();
            serverThreads[i]=new Thread(new MulticastServerThread(current.getMulticastServer(),peerAdvList));
            
            i++;
        }
            i=0;
            for(Thread currentThread:serverThreads)
            {
                System.out.println("Execution Thread :"+ (i++));
                currentThread.start();
                
            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                System.out.println(ex.getMessage());
            
            


      //  }
        
    }



    public Mediator getMediator() {
        return this.confMediator;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        upperPanel = new javax.swing.JPanel();
        lowerpanel = new javax.swing.JPanel();
        midpanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setBorder(javax.swing.BorderFactory.createTitledBorder("ConferencePanel"));
        setLayout(new java.awt.BorderLayout());

        upperPanel.setLayout(new java.awt.BorderLayout());
        add(upperPanel, java.awt.BorderLayout.PAGE_START);
        add(lowerpanel, java.awt.BorderLayout.PAGE_END);

        midpanel.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setTabSize(3);
        jTextArea1.setEnabled(false);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout midpanelLayout = new javax.swing.GroupLayout(midpanel);
        midpanel.setLayout(midpanelLayout);
        midpanelLayout.setHorizontalGroup(
            midpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );
        midpanelLayout.setVerticalGroup(
            midpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
        );

        add(midpanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

  
    public void receive(String message, Colleague sender, Object body) {
       if(message.equalsIgnoreCase(ConferenceMediator.CONT_SELECTION_CHANGED)) {

           lowerpanel.removeAll();
           if(sender instanceof ContactListPanel) {
              System.out.println("Loading contact info "+sender.getClass());
               upperPanel.removeAll();
               UserInfoPanel uinfo=new UserInfoPanel();
               if(body instanceof PeerResolver) {
                   uinfo.updateInfo((PeerResolver) body);
               }
               else
               {
                   JPanel jPanel=(JPanel)body;
                   currentSelectedPeer=((JLabel)jPanel.getComponent(0)).getText();
                  //Have to apply threading for this operation otherwise pipe is not resolved

                   privateMsgManager.addReceiver(currentSelectedPeer);
                   privateMsgManager.addSender(currentSelectedPeer);
                   
                  
               }
               upperPanel.add(uinfo,BorderLayout.CENTER);
               upperPanel.validate();

               upperPanel.setVisible(true);
           }
           
           else if(sender instanceof GroupsPanel ) {
                upperPanel.removeAll();

                ProtectedPeerGroup selectedGroup=(ProtectedPeerGroup)body;
                currentGroup=selectedGroup;
                gPanel=new GroupInfoPanel(selectedGroup);
                upperPanel.add(gPanel,BorderLayout.CENTER);
                upperPanel.validate();
                upperPanel.setVisible(true);
           }

           upperPanel.setBorder(BorderFactory.createEtchedBorder());
           lowerpanel.setBorder(BorderFactory.createEtchedBorder());
           sendTextMsgPanel.setMediator(confMediator);
           lowerpanel.add(sendTextMsgPanel);
           lowerpanel.validate();
           lowerpanel.setVisible(true);
         
           jScrollPane1.setVisible(true);
           
          
       }

        else if (message.equalsIgnoreCase(ConferenceMediator.JOIN_GROUP)) {
            System.out.println("Group Joined successfully....");
            gPanel.updateComponents();
            ProtectedPeerGroup peerGroup=(ProtectedPeerGroup)body;
            multicastClients.put(peerGroup.getPeerGroup(),
                    new MulticastClient(peerGroup.getPeerGroup(), AppMainFrame.getUserName()));
            mapperArrayList.add(new MulticastToPeerGroupMapper(peerGroup,
                    new MulticastServer(peerGroup.getPeerGroup())));

            startAllMulticastClientThread();
            startAllMulticastServerThread();
             

        }
        else if(message.equalsIgnoreCase(ConferenceMediator.SEND_TEXT_MSG)){

           
            if(privateMode) {
//                JOptionPane.showMessageDialog(null, "private mode"+body);
            sendTextMesssage(body.toString());
           }
             else {
//                 JOptionPane.showMessageDialog(null, "sending msg"+body);
                sendTextMessage(body.toString(), currentGroup.getPeerGroup());
             }
       }
       else if(message.equalsIgnoreCase(ConferenceMediator.RECEIVE_TEXT_MSG)){
           JOptionPane.showMessageDialog(null, "msg receivedx"+body);
           receiveTextMessage(body.toString());
         } 
    }
    public void sendTextMesssage(String message) {
        printMessage(AppMainFrame.getUserName(),message); //print msg first
       //send to receiver
        privateMsgManager.sendDataToReceiver(message, currentSelectedPeer);

       
    }
    public void sendTextMessage(String message,PeerGroup peerGroup)
    {
        printMessage(AppMainFrame.getUserName(),message);
        MulticastClient currentClient=multicastClients.get(peerGroup);
        currentClient.sendMesssage(message);
        
    }

    public void printMessage(String userName,String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Date currentDate=Calendar.getInstance().getTime();
        String dateString=sdf.format(currentDate);
        TextMessage msg=new TextMessage(userName, message, dateString);
        textData.put(currentSelectedPeer,msg);
        message=message.replaceAll("\n","");
        String[] invWords=message.split(" ");
        String formattedStr="";

        int c=1;

        for(int k=0;k<invWords.length;k++){
            StringBuilder currentStr=new StringBuilder(invWords[k]);
            if(currentStr.length()>100) {
                currentStr.insert(50, "\n");
                currentStr.insert(100, "\n");
            }
            else if(currentStr.length() > 40) {
                currentStr.insert(50, "\n");
            }
            formattedStr+=currentStr.toString()+" ";
            if(formattedStr.length()>50*(c)) {
                formattedStr+="\n\t\t\t\t";
                c++;
            }

        }
        int q=1+message.length()/50;
        int rem=6*(q-1)+65-formattedStr.length()%50;

        for(int i=0;i<rem;i++) {
            formattedStr+="  ";
        }

        //String userName=AppMainFrame.getUserName();
        int ulen=userName.length();

        if(ulen<15){
           int nrem=15-ulen;
           for(int j=0;j<nrem;j++) {
               userName+=" ";
           }
        }

        jTextArea1.append("\t"+userName+"\t\t"+formattedStr+dateString+"\n");
    }


    public void receiveTextMessage(String message) {
       //to be called once the message is received. just print the received msg in the textarea
        printMessage(message,currentSelectedPeer);
    }

    
    
    private HashMap<String,TextMessage> textData=new HashMap<String,TextMessage>();
    
    private SendTextMessagePanel sendTextMsgPanel=new SendTextMessagePanel();
    private GroupInfoPanel gPanel;
    private String currentSelectedPeer="";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel lowerpanel;
    private javax.swing.JPanel midpanel;
    private javax.swing.JPanel upperPanel;
    // End of variables declaration//GEN-END:variables
     private ConferenceManager confManager;
    
     
}
