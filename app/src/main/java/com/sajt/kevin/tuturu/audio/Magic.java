package com.sajt.kevin.tuturu.audio;

import android.os.Environment;

import com.sajt.kevin.tuturu.math.DSPC;
import com.sajt.kevin.tuturu.math.FFT;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Magic {

    public static void magic(String audio1, String audio2) {

        double[] sajt1 = FFT.fftMagnitude(nextPowOfTwo(toDoubleArray(readAudioFile(audio1))));
        double[] sajt2 = FFT.fftMagnitude(nextPowOfTwo(toDoubleArray(readAudioFile(audio2))));

        int sajt = 100;

        double[] sajt3 = new double[sajt];
        double[] sajt4 = new double[sajt];
        for (int i=0;i<sajt;i++) {
            sajt3[i] = sajt1[i];
            sajt4[i] = sajt2[i];
        }
        double[] alma = DSPC.xcorr(sajt3, sajt4);

        for (double krumpli : alma) {
            System.out.println("krumpli: " + krumpli);
        }

    }

    // converts byte array to double array
    private static double[] toDoubleArray(byte[] byteArray){
        int SAMPLE_RESOLUTION = 16;
        int BYTES_PER_POINT = SAMPLE_RESOLUTION / 8;
        int[] vals = new int[byteArray.length/BYTES_PER_POINT];
        double[] Ys = new double[byteArray.length / BYTES_PER_POINT];
        //double[] Xs = new double[byteArray.length / BYTES_PER_POINT];
        //double[] Xs2 = new double[byteArray.length / BYTES_PER_POINT];
        byte hByte;
        byte lByte;

        for (int i=0; i<vals.length; i++)
        {
            // bit shift the byte buffer into the right variable format
            hByte = byteArray[i * 2 + 1];
            lByte = byteArray[i * 2 + 0];
            vals[i] = (int)(short)((hByte << 8) | lByte);
            //Xs[i] = i;
            Ys[i] = vals[i];
            //Xs2[i] = (double)i/Ys.length*RECORDER_SAMPLE_RATE/1000.0; // units are in kHz
        }

        //signal filtering
//        double[] magicYs = new double[Ys.length];
//        for (int i=0; i<magicYs.length-1; i++)
//        {
//            magicYs[i] = Ys[i+1] - 0.95 * Ys[i];
//        }
//        return magicYs;
        return Ys;
    }

    // reads raw audio file into byte array
    private static byte[] readAudioFile(String audioFileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + audioFileName + ".pcm");
        byte[] byteData = new byte[(int) file.length()];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(byteData);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteData;
    }

    private static double[] nextPowOfTwo(double[] array) {
        int nextpowtwo = 1;
        while (true) {
            if (array.length < nextpowtwo) {
                break;
            } else {
                nextpowtwo *= 2;
            }
        }
        double[] sampleBuffer = new double[nextpowtwo];

        for (int i =0;i<array.length;i++) {
            sampleBuffer[i] = array[i];
        }
        System.out.println("nextpowoftwo: " + sampleBuffer.length);

        return sampleBuffer;
    }

    public static double roundDown6(double d) {
        return ((long)(d * 1e6)) / 1e6;
        //Long typecast will remove the decimals
    }

}
