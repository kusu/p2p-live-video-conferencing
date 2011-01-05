
package org.ioe.bct.p2pconference.core;

import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;

import java.io.*;
import java.util.Arrays;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.ModuleSpecAdvertisement;


public class SocketClient {

    /**
     * number of runs to make
     */
    private final static long RUNS = 8;

    /**
     * number of iterations to send the payload
     */
    private final static long ITERATIONS = 1000;

    /**
     * payload size
     */
    private final static int PAYLOADSIZE = 64 * 1024;

    
    private DiscoveryService ds;
    private transient PeerGroup netPeerGroup = null;
    private transient PipeAdvertisement pipeAdv;
    private transient boolean waitForRendezvous = false;
    private PipeAdvertisement advertisement;
    private JxtaSocket socket;
    private boolean isConnected=true;
    private String me;
    private String sender;

    public SocketClient(P2PNetworkCore manager,String me,String sender) {
        this.me=me;
        this.sender=sender;
        netPeerGroup = manager.getNetPeerGroup();
        ds=netPeerGroup.getDiscoveryService();
        
    }

     public PipeAdvertisement getPipeAdvertisement()
    {
        advertisement=null;
        System.out.println("Am i even here too"); 
        try {
            Enumeration<Advertisement> advertisements = ds.getLocalAdvertisements(DiscoveryService.ADV, "Name", sender+me+"audioModuleSpec");
            if(advertisements!=null)
            {
                while(advertisements.hasMoreElements())
                {
                    ModuleSpecAdvertisement myModuleSpecAdv = (ModuleSpecAdvertisement)advertisements.nextElement();
                    advertisement=myModuleSpecAdv.getPipeAdvertisement();
                    JOptionPane.showMessageDialog(null, advertisement);
                    break;
                }
            }
            else
            {

                class ServiceListener implements DiscoveryListener{
                    private PipeAdvertisement localAds=null;
                    public PipeAdvertisement getAdvertisement()
                    {
                        return localAds;
                    }
                    public void discoveryEvent(DiscoveryEvent event) {
                        System.out.println("Am i ever here");
                        Enumeration enumm;
                        PipeAdvertisement pipeAdv = null;
                        String str;
                        DiscoveryResponseMsg myMessage = event.getResponse();
                        enumm = myMessage.getResponses();
                        while(enumm.hasMoreElements())
                        {
                            str = (String)enumm.nextElement();
                        try {
                            ModuleSpecAdvertisement myModSpecAdv = (ModuleSpecAdvertisement) AdvertisementFactory.newAdvertisement(MimeMediaType.XMLUTF8,new ByteArrayInputStream(str.getBytes()));
                            localAds=myModSpecAdv.getPipeAdvertisement();
                            JOptionPane.showMessageDialog(null,localAds);
                            break;

                      } catch(Exception ee) {
                          ee.printStackTrace();
             //             System.exit(-1);
                      }
                        }
                    }

                }
                ServiceListener myDiscoveryListener=new ServiceListener();
                advertisement=myDiscoveryListener.getAdvertisement();
                ds.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", sender+me+"audioModuleSpec", 1,myDiscoveryListener);
            }

        } catch (IOException ex) {
            Logger.getLogger(MulticastSocketService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return advertisement;

    }

    /**
     * Interact with the server.
     */
    public void runClient() {
        
          
            System.out.println("Connecting to the server");
        try {
            JOptionPane.showMessageDialog(null,advertisement);
            socket = new JxtaSocket(netPeerGroup, null, advertisement, 0, true);
            Capture audioDataSource=new Capture();
            Thread clientThread=new Thread(new ConnectionHandler(audioDataSource));
            clientThread.start();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private class ConnectionHandler implements Runnable{

        StreamDataSourceInterface streamDataSource=null;
        InputStream in;
        DataInput dis ;
        public ConnectionHandler(StreamDataSourceInterface streamDS)
        {
            streamDataSource=streamDS;
            try {
                in = socket.getInputStream();
                dis = new DataInputStream(in);

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        }
        public void receiveData()
    {
            
        byte[] in_buf = new byte[PAYLOADSIZE];
        try {
            dis.readFully(in_buf);
            streamDataSource.setData(in_buf);
            
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }

        public void run() {
            while(isConnected)
            {
                try {
                    receiveData();
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }

            }
        }
    }
    

    
}

