package org.ioe.bct.p2pconference.core;

import net.jxta.endpoint.Message;
import net.jxta.endpoint.Message.ElementIterator;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.WireFormatMessage;
import net.jxta.endpoint.WireFormatMessageFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.CountingOutputStream;
import net.jxta.util.DevNullOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.Date;


/**
 * This tutorial illustrates the use of JXTA Pipes to exchange messages.
 * <p/>
 * This peer is the pipe "server". It opens the pipe for input and waits for
 * messages to be sent. Whenever a Message is received from a "client" the
 * contents are printed.
 */
public class PeerMsgReceiver {

    
}
