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
    public ByteArrayOutputStream[] inFromNetwork;
    public ByteArrayOutputStream[] inFromNetworkNext;
    private ByteArrayOutputStream[] tempBufferStream;
    private ByteArrayOutputStream[] tempBufferStreamOut;
    private ByteArrayOutputStream[] tempInBufferStream;
    private ByteArrayOutputStream[] tempInBufferStreamNext;
    private Codec codecClass;
    private int writerDuringCapturePointer = 0;
    private int readerDuringSendingPointer = 0;
    private int writerDuringReceivingPointer = 0;
    private int readerDuringPlayingPointer=0;
    private int cycle=0;
    private int readerInCycle=0;
    private int cycleNext=0;
    private int readerInCycleNext=0;
    private boolean startTransmission=false;
  public CaptureNew() {
       inFromNetwork=new ByteArrayOutputStream[10];
       inFromNetworkNext=new ByteArrayOutputStream[10];
       outToNetwork = new ByteArrayOutputStream[10];
       outToNetworkNext = new ByteArrayOutputStream[10];
       tempBufferStream=outToNetwork;
       tempBufferStreamOut=outToNetwork;
       tempInBufferStream=inFromNetwork;
       tempInBufferStreamNext=inFromNetwork;
       codecClass=new Codec();
  }

  public void captureAudio() {
    try {
      
      final AudioFormat format = getFormat();
      DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
      final TargetDataLine line = (TargetDataLine)AudioSystem.getLine(info);
      line.open(format);
      line.start();
      
      int bufferSize = (int)format.getSampleRate()* format.getFrameSize();
      byte buffer[]=new byte[93440];
      byte compressedBuffer[]=null;
      running = true;
      
      ByteArrayOutputStream out=new ByteArrayOutputStream();
      while(true)
      {
      int count = line.read(buffer, 0, buffer.length);
      for(int i=0;i<(count/320);i++)
      {
        byte tempBuffer[]=new byte[320];
        for(int j=0;j<320;j++)
        {
            tempBuffer[j]=buffer[i*320+j];
        }
          compressedBuffer=codecClass.encode(tempBuffer);
          out.write(compressedBuffer,0,compressedBuffer.length);
      }
      System.out.println(out.size());
      byte compressedFullBuffer[]=out.toByteArray();
      out.reset();
      if (count > 0) {
       System.out.println("Capturing Sound......");
            if(writerDuringCapturePointer<9)
            {
                tempBufferStream[writerDuringCapturePointer]=new ByteArrayOutputStream();
                tempBufferStream[writerDuringCapturePointer].write(compressedFullBuffer, 0, compressedFullBuffer.length);
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
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CaptureNew.class.getName()).log(Level.SEVERE, null, ex);
                }
      }
      } catch (LineUnavailableException e) {
          System.err.println("Line unavailable: " + e);
          System.exit(-2);
    }
  }
  public int getDifference()
    {
      if(readerInCycle==cycle)
      {
          return Math.abs(writerDuringCapturePointer-readerDuringSendingPointer);
      }
     else {
            return 10+(writerDuringCapturePointer-readerDuringSendingPointer);
     }
  }
public byte[] getCapturedData()
{
    byte msg[]=null;
    byte msg1[]=new byte[320];
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
public void setReceivedData(byte msg[])
{
   int length=msg.length;
   byte decompressedBuffer[]=null;
   ByteArrayOutputStream decompressedStream=new ByteArrayOutputStream();
   for(int i=0;i<length/28;i++)
   {
       byte[] temp=new byte[28];
       for(int j=0;j<28;j++)
       {
           temp[j]=msg[i*28 + j];
       }
       decompressedBuffer=codecClass.decode(temp);
       decompressedStream.write(decompressedBuffer,0,decompressedBuffer.length);
  }
   byte decompressedFullBuffer[]=decompressedStream.toByteArray();
   if(writerDuringReceivingPointer<9)
   {
       tempInBufferStream[writerDuringReceivingPointer]=new ByteArrayOutputStream();
       tempInBufferStream[writerDuringReceivingPointer].write(decompressedFullBuffer,0,decompressedFullBuffer.length);
       writerDuringReceivingPointer++;
   }
   else
   {
        cycleNext=(cycleNext+1)%2;
        writerDuringReceivingPointer=0;
        if(cycleNext==1)
        {
            tempInBufferStream=inFromNetworkNext;
        }
        else
        {
            tempInBufferStream=inFromNetwork;
        }
   }

}
  public void playAudio() {
    try {
      byte audio[]=null;
      if(readerDuringPlayingPointer >= writerDuringReceivingPointer && readerInCycleNext==cycleNext)
      {
          return;
      }
      if(readerDuringPlayingPointer<9)
      {
            audio = tempInBufferStreamNext[readerDuringPlayingPointer].toByteArray();
            System.out.println("Playing audio");
      }
      else
        {
           readerInCycleNext=(readerInCycleNext+1)%2;
           readerDuringPlayingPointer=0;
           if(readerInCycleNext==1)
           {
               tempInBufferStreamNext=inFromNetworkNext;
           }
           else
           {
                tempInBufferStreamNext=inFromNetwork;
           }
        }
      InputStream input = new ByteArrayInputStream(audio);
      final AudioFormat format = getFormat();
      final AudioInputStream ais = new AudioInputStream(input, format,audio.length / format.getFrameSize());
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
      final SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
      line.open(format);
      line.start();
      int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
      byte buffer[] = new byte[8176];
      try {
        int count;
        while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
          if (count > 0) {
            line.write(buffer, 0, count);
          }
        }

        line.drain();
        line.close();
       
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
    float sampleRate = 8000;
    int sampleSizeInBits = 16;
    int channels = 2;
    boolean signed = true;
    boolean bigEndian = true;
    return new AudioFormat(sampleRate,sampleSizeInBits, channels, signed, bigEndian);
    }

}
