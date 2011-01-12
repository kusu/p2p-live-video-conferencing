/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Administrator
 */
public class CaptureNew {
protected boolean running;
    public ByteArrayOutputStream[] outToNetwork;
    public ByteArrayOutputStream[] outToNetworkNext;
    public ByteArrayOutputStream inFromNetwork;
    private ByteArrayOutputStream[] tempBufferStream;
    private ByteArrayOutputStream[] tempBufferStreamOut;

    private int writerDuringCapturePointer = 0;
    private int readerDuringSendingPointer = 0;
    private int cycle=0;
    private int readerInCycle=0;
    private boolean startTransmission=false;
  public CaptureNew() {
       inFromNetwork=new ByteArrayOutputStream();
       outToNetwork = new ByteArrayOutputStream[10];
       outToNetworkNext = new ByteArrayOutputStream[10];
       tempBufferStream=outToNetwork;
       tempBufferStreamOut=outToNetwork;
  }

  public void captureAudio() {
    try {
      
      final AudioFormat format = getFormat();
      DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
      final TargetDataLine line = (TargetDataLine)AudioSystem.getLine(info);
      line.open(format);
      line.start();
      
      int bufferSize = (int)format.getSampleRate()* format.getFrameSize();
      byte buffer[]=new byte[8192];
      running = true;
      while(true)
      {
      int count = line.read(buffer, 0, buffer.length);
      if (count > 0) {
       System.out.println("Capturing Sound......");
            if(writerDuringCapturePointer<9)
            {
                tempBufferStream[writerDuringCapturePointer]=new ByteArrayOutputStream();
                tempBufferStream[writerDuringCapturePointer].write(buffer, 0, count);
                writerDuringCapturePointer++;
                startTransmission=true;
            }
            else
            {
                cycle=(cycle+1)%2;
                writerDuringCapturePointer=0;
                if(cycle==1)
                {
                    tempBufferStream=outToNetworkNext;

                }
                else
                {
                    tempBufferStream=outToNetwork;
                }
            }
            
       }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CaptureNew.class.getName()).log(Level.SEVERE, null, ex);
                }
      }
      } catch (LineUnavailableException e) {
          System.err.println("Line unavailable: " + e);
          System.exit(-2);
    }
  }
public byte[] getCapturedData()
{
    byte msg[]=null;
    byte msg1[]=new byte[8192];
    if(startTransmission)
    {
    if(readerDuringSendingPointer<9)
    {
        if(tempBufferStreamOut[readerDuringSendingPointer]!=null)
        {
        msg=tempBufferStreamOut[readerDuringSendingPointer].toByteArray();
        readerDuringSendingPointer++;
        }
    }
    else
    {
        readerInCycle=(readerInCycle++)%2;
        readerDuringSendingPointer=0;
        if(readerInCycle==1)
        {
            tempBufferStreamOut=outToNetworkNext;

        }
        else{
            tempBufferStreamOut=outToNetwork;
        }
    }
    }
    if(msg==null) return msg1;
    return msg;
}
  public void playAudio() {
    try {
      byte audio[] = inFromNetwork.toByteArray();
      InputStream input = new ByteArrayInputStream(audio);
      final AudioFormat format = getFormat();
      final AudioInputStream ais = new AudioInputStream(input, format,audio.length / format.getFrameSize());
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
      final SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
      line.open(format);
      line.start();
      int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
      byte buffer[] = new byte[8192];
      try {
        int count;
        while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
          if (count > 0) {
            line.write(buffer, 0, count);
          }
        }

        line.drain();
        line.close();
        inFromNetwork.reset();
      } catch (IOException e) {
            System.err.println("I/O problems: " + e);
            System.exit(-3);
      }
      
      } catch (LineUnavailableException e) {
          System.err.println("Line unavailable: " + e);
          System.exit(-4);
      }
  }

  private AudioFormat getFormat() {
    float sampleRate = 48000;
    int sampleSizeInBits = 16;
    int channels = 2;
    boolean signed = true;
    boolean bigEndian = true;
    return new AudioFormat(sampleRate,sampleSizeInBits, channels, signed, bigEndian);
    }

  public static void main(String args[])
  {
      
  }
}
