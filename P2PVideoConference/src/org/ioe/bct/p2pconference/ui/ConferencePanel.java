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

import org.ioe.bct.p2pconference.ui.controls.ConferenceMediator;
import org.ioe.bct.p2pconference.ui.controls.ConferenceManager;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PeerAdvertisement;
import org.ioe.bct.p2pconference.AppLoader;
import org.ioe.bct.p2pconference.core.JXTAPeerGroupOrganizer;
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
    private P2PNetworkCore networkManager;
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
        networkManager=netCore;
        privateMsgManager=new PrivateMsgManager(AppMainFrame.getUserName(),netCore , confMediator);
    }
    public void setMediator(Mediator m) {
        this.confMediator=m;
        confMediator.addColleague(this);
    }
    private class MulticastServerThread implements Runnable{

        private MulticastServer server;
        private ArrayList<PeerAdvertisement> peerAdvList;
//        private String message;
        private long sleepTimer=1000;
        public MulticastServerThread(MulticastServer multicastServer,ArrayList<PeerAdvertisement> peerAdvs)
        {
            this.server=multicastServer;
            this.peerAdvList=peerAdvs;
        }

         synchronized public void maximizePriority() {
             for(Thread current:serverThreads) {
                    current.setPriority(Thread.MAX_PRIORITY);
             }
            sleepTimer=1000;

        }
        synchronized public void minimizePriority() {
            for(Thread current:serverThreads) {
                 current.setPriority(Thread.NORM_PRIORITY);
            }
            sleepTimer=10000;
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
//                        message = new String(buffer);
//                        printMessage(peerAdv.getName(), message);
                    }
                   
                    sleep(sleepTimer);
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
        private long sleepTimer=1000;

        public MulticastClientThread(Collection<MulticastClient> mcastClients)
        {
            this.multicastClients=mcastClients;
        }

        synchronized public void maximizePriority() {
            clientThread.setPriority(Thread.MAX_PRIORITY);
            sleepTimer=1000;
            
        }
        synchronized public void minimizePriority() {
            clientThread.setPriority(Thread.NORM_PRIORITY);
            sleepTimer=10000;
        }

        public void run() {
            while(true)
            {
                
                    Iterator<MulticastClient> it = multicastClients.iterator();
                    while (it.hasNext()) {
                        MulticastClient mcastClient = it.next();
                        mcastClient.publishPipeAdvertisement();
                    
                   
                }
                   sleep(sleepTimer);
            }
        }
        
    }
    public void startAllMulticastClientThread()
    {
        MulticastClientThread threadObj=new MulticastClientThread(multicastClients.values());
        clientThread=new Thread(threadObj);
        System.out.println("Execution Client Thread :");
        clientThread.start();
        threadObj.maximizePriority();
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
//            Iterator<PeerAdvertisement> it=peerAdvList.iterator();
            MulticastServerThread threadInst=new MulticastServerThread(current.getMulticastServer(),peerAdvList);
            serverThreads[i]=new Thread(threadInst);
            threadInst.maximizePriority();
            i++;
        }
            i=0;
            for(Thread currentThread:serverThreads)
            {
                System.out.println("Execution Thread :"+ (i++));
                currentThread.start();
                
            }
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
               UserInfoPanel uinfo=new UserInfoPanel(networkManager,confMediator);
              
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
                   uinfo.setPeer(currentSelectedPeer);
                  
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
                gPanel.setMediator(confMediator);
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
                    new MulticastServer(peerGroup.getPeerGroup(),confMediator)));
            AppLoader.mainFrame.getJXTAPeerGroupOrganizer().slowPublishAndDiscovery();

            
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
           if(privateMode) {
//           JOptionPane.showMessageDialog(null, "msg receivedx"+body);
           System.out.println("Message received: "+body.toString());
               receiveTextMessage(body.toString());

           }
             else {
               TextMessage rMsg=(TextMessage)body;
                receiveTextMessage(rMsg.getForm(),rMsg.getMessage());
             }
         } else if(message.equals(ConferenceMediator.PRIVATE_VOICE_CALL_SYNC)) {
             privateMsgManager.sendDataToReceiver(body.toString()+"\n"+AppMainFrame.getUserName(), currentSelectedPeer);
         }else if(message.equals(ConferenceMediator.PRIVATE_CALL_ACCEPT_MSGQ)||
         message.equals(ConferenceMediator.PRIVATE_CALL_REJECT_MSGQ)) {
             String code=body.toString().split("\n")[0];
             String to=body.toString().split("\n")[1];
             privateMsgManager.sendDataToReceiver(code+"\n"+AppMainFrame.getUserName(),to);
         }

    }

    public void receiveTextMessage(String peerName,String msg) {
        printMessage(msg,peerName);
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
        String[] parts=message.split("\n");
        String messageText=parts[0];
        System.out.println(message);
        String audioCallRequester=parts[1];
        if(messageText.equals(ConferenceMediator.AUDIO_REQUEST_CODE)){
            JWindow window=new JWindow(AppLoader.mainFrame);
            window.setBounds(400, 300, 200, 100);
            window.getContentPane().add(new CallResponsePanel(audioCallRequester,confMediator));
            window.pack();
            window.setVisible(true);
            return;
        }else if(messageText.equals(ConferenceMediator.PRIVATE_CALL_ACCEPT_CODE)) {
            confMediator.sendMessage(ConferenceMediator.PRIVATE_CALL_ACCPTED, this, null);
        }else if(messageText.equals(ConferenceMediator.PRIVATE_CALL_REJECT_CODE)) {
            confMediator.sendMessage(ConferenceMediator.PRIVATE_CALL_REJECTED, this, null);
        }

        printMessage(messageText,currentSelectedPeer);
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
