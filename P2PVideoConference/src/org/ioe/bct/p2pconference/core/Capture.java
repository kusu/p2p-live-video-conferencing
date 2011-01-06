/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Administrator
 */
public class Capture implements StreamDataSourceInterface{
    protected boolean running;
    ByteArrayOutputStream outToNetwork;
    ByteArrayOutputStream inFromNetwork;
  public Capture() {
       inFromNetwork=new ByteArrayOutputStream();
  }

  private void captureAudio() {
    try {
      final AudioFormat format = getFormat();
      DataLine.Info info = new DataLine.Info(
        TargetDataLine.class, format);
      final TargetDataLine line = (TargetDataLine)
        AudioSystem.getLine(info);
      line.open(format);
      line.start();
      Runnable runner = new Runnable() {
        int bufferSize = (int)format.getSampleRate()
          * format.getFrameSize();
        byte buffer[] = new byte[bufferSize];

        public void run() {
          while(true)
          {
            outToNetwork = new ByteArrayOutputStream();
          running = true;
          int count =
            line.read(buffer, 0, buffer.length);
          if (count > 0) {
                try {
                    outToNetwork.write(buffer, 0, count);
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Capture.class.getName()).log(Level.SEVERE, null, ex);
                }
          }
         }
        }
      };
      Thread captureThread = new Thread(runner);
      captureThread.start();
    } catch (LineUnavailableException e) {
      System.err.println("Line unavailable: " + e);
      System.exit(-2);
    }
  }

  public void playAudio() {
    try {
      byte audio[] = inFromNetwork.toByteArray();
      InputStream input =      
        new ByteArrayInputStream(audio);
      final AudioFormat format = getFormat();
      final AudioInputStream ais =
        new AudioInputStream(input, format,
        audio.length / format.getFrameSize());
      DataLine.Info info = new DataLine.Info(
        SourceDataLine.class, format);
      final SourceDataLine line = (SourceDataLine)
        AudioSystem.getLine(info);
      line.open(format);
      line.start();

      Runnable runner = new Runnable() {
        int bufferSize = (int) format.getSampleRate()
          * format.getFrameSize();
        byte buffer[] = new byte[bufferSize];

        public void run() {
          try {
            int count;
            while ((count = ais.read(
                buffer, 0, buffer.length)) != -1) {
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
        }
      };
      Thread playThread = new Thread(runner);
      playThread.start();
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
    return new AudioFormat(sampleRate,
      sampleSizeInBits, channels, signed, bigEndian);
  }

    public byte[] getData() {
        captureAudio();
        return outToNetwork.toByteArray();
    }
    public void setData(byte buf[])
    {
        inFromNetwork.write(buf, 0, buf.length);
        playAudio();
    }
}

