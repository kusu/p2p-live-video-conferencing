/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.xiph.speex.SpeexDecoder;
import org.xiph.speex.SpeexEncoder;


/**
 *
 * @author Tsuman
 */
public class Codec {
    private static SpeexEncoder speexEnc;
    private static SpeexDecoder speexDec;
    private static boolean bigEndian=true;
    
    public byte[] encode(byte[] buffer){
        if(speexEnc == null){
            speexEnc = new SpeexEncoder();
            speexEnc.init(0, 5, 8000, 1);
        }
        if (bigEndian)
            switchEndianness(buffer);

        speexEnc.processData(buffer,0, buffer.length);

        //System.out.println(speexEnc.getProcessedDataByteSize());
        byte[] out = new byte[speexEnc.getProcessedDataByteSize()];
        speexEnc.getProcessedData(out, 0);

        return out;
    }

    public byte[] decode(byte[] buffer){
        if (speexDec == null){
            speexDec = new SpeexDecoder();
            speexDec.init(0, 8000, 1, true);
        }

        try{
            speexDec.processData(buffer,0, buffer.length);
        }
        catch(Exception e){
            //handle exception
        }

        byte[] ret = new byte[speexDec.getProcessedDataByteSize()];
        speexDec.getProcessedData(ret, 0);

        if(bigEndian)
            switchEndianness(buffer);

        return ret;
    }

    public static void switchEndianness(byte[] samples) {
        for (int i = 0; i < samples.length; i += 2) {
            byte tmp = samples[i];
            samples[i] = samples[i + 1];
            samples[i + 1] = tmp;
        }
    }

   
}
