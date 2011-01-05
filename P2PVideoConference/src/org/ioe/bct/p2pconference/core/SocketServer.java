package org.ioe.bct.p2pconference.core;


import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import net.jxta.discovery.DiscoveryService;
import net.jxta.peergroup.NetPeerGroupFactory;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.ModuleClassID;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;


/**
 * This tutorial illustrates the use JxtaServerSocket It creates a
 * JxtaServerSocket with a back log of 10. it also blocks indefinitely, until a
 * connection is established.
 * <p/>
 * Once a connection is established data is exchanged with the initiator.
 * The initiator will provide an iteration count and buffer size. The peers will
 * then read and write buffers. (or write and read for the initiator).
 */
public class SocketServer {

    private transient PeerGroup netPeerGroup = null;
    public final static String SOCKETIDSTR = "urn:jxta:uuid-59616261646162614E5047205032503393B5C2F6CA7A41FBB0F890173088E79404";
    private ModuleClassID myService1ID = null;
    private ModuleClassAdvertisement myService1ModuleAdvertisement;
    private ModuleSpecAdvertisement myModuleSpecAdvertisement;
    private PipeAdvertisement socketAdvertisement;
    private DiscoveryService ds;
    private JxtaServerSocket serverSocket = null;
    private boolean serverConnection=true;
    public SocketServer() throws IOException, PeerGroupException {
        NetworkManager manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, "SocketServer",
                new File(new File(".cache"), "SocketServer").toURI());
        manager.startNetwork();
        netPeerGroup = manager.getNetPeerGroup();
    }

    public void buildModuleAdvertisement() {
	 myService1ModuleAdvertisement = (ModuleClassAdvertisement) AdvertisementFactory.newAdvertisement(ModuleClassAdvertisement.getAdvertisementType());

	 myService1ModuleAdvertisement.setName("Multicasting");
	 myService1ModuleAdvertisement.setDescription("receiveing data on");

       myService1ID = IDFactory.newModuleClassID();
	 myService1ModuleAdvertisement.setModuleClassID(myService1ID);


       try {
  	   ds.publish(myService1ModuleAdvertisement);
	   ds.remotePublish(myService1ModuleAdvertisement);
       } catch (Exception e) {
         System.out.println("Error during publish of Module Advertisement");
         System.exit(-1);
       }
    }
     public void publishModuleAdvertisement()
    {
         try {
  	   ds.publish(myService1ModuleAdvertisement);
	   ds.remotePublish(myService1ModuleAdvertisement);
       } catch (Exception e) {
         System.out.println("Error during publish of Module Advertisement");
         System.exit(-1);
       }
     }
     public void buildModuleSpecificationAdvertisement(PipeAdvertisement myPipeAdvertisement) {

//	StructuredTextDocument paramDoc = (StructuredTextDocument)StructuredDocumentFactory.newStructuredDocument(new MimeMediaType("text/xml"),"Parm");
//	StructuredDocumentUtils.copyElements(paramDoc, paramDoc, (Element)myPipeAdvertisement.getDocument(new MimeMediaType("text/xml")));


         myModuleSpecAdvertisement = (ModuleSpecAdvertisement) AdvertisementFactory.newAdvertisement(ModuleSpecAdvertisement.getAdvertisementType());

	myModuleSpecAdvertisement.setName("modulespec");
	myModuleSpecAdvertisement.setVersion("Version 1.0");
	myModuleSpecAdvertisement.setCreator("p2pvideoconference");
	myModuleSpecAdvertisement.setModuleSpecID(IDFactory.newModuleSpecID(myService1ID));
	myModuleSpecAdvertisement.setSpecURI("www.ioe.edu.np");
//      myModuleSpecAdvertisement.setParam((StructuredDocument) paramDoc);
      myModuleSpecAdvertisement.setPipeAdvertisement(myPipeAdvertisement);


      try {
        ds.publish(myModuleSpecAdvertisement);
        ds.remotePublish(myModuleSpecAdvertisement);
      } catch (Exception e) {
         System.out.println("Error during publish of Module Specification Advertisement");
         e.printStackTrace();
         System.exit(-1);
      }
    }
    public void publishModuleSpecificationAdvertisement()
    {
        try {
        ds.publish(myModuleSpecAdvertisement);
        ds.remotePublish(myModuleSpecAdvertisement);
      } catch (Exception e) {
         System.out.println("Error during publish of Module Specification Advertisement");
         e.printStackTrace();
         System.exit(-1);
      }

    }
    public PipeAdvertisement createSocketAdvertisement() {
       
       PipeAdvertisement advertisement = (PipeAdvertisement)
                AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());

        advertisement.setPipeID(IDFactory.newPipeID(netPeerGroup.getPeerGroupID()));
        advertisement.setType(PipeService.UnicastType);
        advertisement.setName("Socket tutorial");
        socketAdvertisement=advertisement;
        return advertisement;
    }

    /**
     * Wait for connections
     */
    public void initializeServer() {

        System.out.println("Starting ServerSocket");
        
        try {
            serverSocket = new JxtaServerSocket(netPeerGroup, socketAdvertisement, 10);
            serverSocket.setSoTimeout(0);
        } catch (IOException e) {
            System.out.println("failed to create a server socket");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    public void runServer()
    {
        boolean flag=true;
        while (flag) {
            try {
                System.out.println("Waiting for connections");
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    System.out.println("New socket connection accepted");
                    Capture audioCapture=new Capture();
                    Thread thread = new Thread(new ConnectionHandler(socket,audioCapture), "Connection Handler Thread");
                    thread.start();
                    flag=false;
                }
               publishModuleAdvertisement();
               publishModuleSpecificationAdvertisement();
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void stopServerConnection()
    {
        serverConnection=false;
    }
    private class ConnectionHandler implements Runnable {
        Socket socket = null;
        OutputStream out;
        StreamDataSourceInterface streamDataSource=null;
        public ConnectionHandler(Socket socket,StreamDataSourceInterface streamDS) {
            this.socket = socket;
            this.streamDataSource=streamDS;
            try {
                out = socket.getOutputStream();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        /**
         * Sends data over socket
         *
         * @param socket the socket
         */
        public void sendData() {
            try {
                byte[] buf=streamDataSource.getData();
                out.write(buf);
                out.flush();
                
            } catch (Exception ie) {
                ie.printStackTrace();
            }
        }

        public void run() {
            while(serverConnection)
            {
                try {
                    sendData();
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    /**
     * main
     *
     * @param args command line args
     */
    
}
