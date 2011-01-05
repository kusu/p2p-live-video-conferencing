
package org.ioe.bct.p2pconference.core;

import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;

import java.io.*;
import java.util.Arrays;
import java.text.MessageFormat;

/**
 * This tutorial illustrates the use JxtaSocket. It attempts to bind a
 * JxtaSocket to an instance of JxtaServerSocket bound socket.adv.
 * <p/>
 * Once a connection is established data is exchanged with the server.
 * The client will identify how many ITERATIONS of PAYLOADSIZE buffers will be
 * exchanged with the server and then write and read those buffers.
 */
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

    private transient NetworkManager manager = null;

    private transient PeerGroup netPeerGroup = null;
    private transient PipeAdvertisement pipeAdv;
    private transient boolean waitForRendezvous = false;

    public SocketClient(boolean waitForRendezvous) {
        try {
            manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, "SocketClient",
                    new File(new File(".cache"), "SocketClient").toURI());
            manager.startNetwork();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        netPeerGroup = manager.getNetPeerGroup();
        
        if (waitForRendezvous) {
            manager.waitForRendezvousConnection(0);
        }
    }

    /**
     * Interact with the server.
     */
    public void run() {
        try {
            if (waitForRendezvous) {
                manager.waitForRendezvousConnection(0);
            }

            long start = System.currentTimeMillis();
            System.out.println("Connecting to the server");
            JxtaSocket socket = new JxtaSocket(netPeerGroup,
                    // no specific peerid
                    null,
                    pipeAdv,
                    // connection timeout: 5 seconds
                    5000,
                    // reliable connection
                    true);

            // get the socket output stream
            OutputStream out = socket.getOutputStream();
            DataOutput dos = new DataOutputStream(out);

            // get the socket input stream
            InputStream in = socket.getInputStream();
            DataInput dis = new DataInputStream(in);

            long total = ITERATIONS * (long) PAYLOADSIZE * 2;
            System.out.println("Sending/Receiving " + total + " bytes.");

            dos.writeLong(ITERATIONS);
            dos.writeInt(PAYLOADSIZE);

            long current = 0;

            while (current < ITERATIONS) {
                byte[] out_buf = new byte[PAYLOADSIZE];
                byte[] in_buf = new byte[PAYLOADSIZE];

                Arrays.fill(out_buf, (byte) current);
                out.write(out_buf);
                out.flush();
                dis.readFully(in_buf);
                assert Arrays.equals(in_buf, out_buf);
                current++;
            }
            out.close();
            in.close();

            long finish = System.currentTimeMillis();
            long elapsed = finish - start;

            System.out.println(MessageFormat.format("EOT. Processed {0} bytes in {1} ms. Throughput = {2} KB/sec.", total, elapsed,
                    (total / elapsed) * 1000 / 1024));
            socket.close();
            System.out.println("Socket connection closed");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void stop() {
        manager.stopNetwork();
    }

    /**
     * If the java property RDVWAIT set to true then this demo
     * will wait until a rendezvous connection is established before
     * initiating a connection
     *
     * @param args none recognized.
     */
    public static void main(String args[]) {

        /*
         System.setProperty("net.jxta.logging.Logging", "FINEST");
         System.setProperty("net.jxta.level", "FINEST");
         System.setProperty("java.util.logging.config.file", "logging.properties");
         */
        try {
            Thread.currentThread().setName(SocketClient.class.getName() + ".main()");
            String value = System.getProperty("RDVWAIT", "false");
            boolean waitForRendezvous = Boolean.valueOf(value);
            SocketClient socEx = new SocketClient(waitForRendezvous);

            for (int i = 1; i <= RUNS; i++) {
                System.out.println("Run #" + i);
                socEx.run();
            }
            socEx.stop();
        } catch (Throwable e) {
            System.out.flush();
            System.err.println("Failed : " + e);
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}

