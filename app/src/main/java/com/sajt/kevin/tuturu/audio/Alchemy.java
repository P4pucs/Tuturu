package com.sajt.kevin.tuturu.audio;

import android.os.Environment;

import com.sajt.kevin.tuturu.math.DSP.Filters;
import com.sajt.kevin.tuturu.math.DSP.FourierTransform;
import com.sajt.kevin.tuturu.math.DSP.MFCC;
import com.sajt.kevin.tuturu.math.DSP.Utilities;
import com.sajt.kevin.tuturu.math.FFT;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Alchemy {

    private static double threshold = 1.1;

    public static boolean alchemy(String audio1, String audio2) {

        double[] signal1 = MFCC.compute(FourierTransform.Spectrum(nextPowOfTwo(hanning(toDouble(readAudioFile(audio1))))));
        double[] signal2 = MFCC.compute(FourierTransform.Spectrum(nextPowOfTwo(hanning(toDouble(readAudioFile(audio2))))));

        double mse = Utilities.MSE(signal1, signal2, signal1.length);

        for (double asd : signal1) {
            System.out.println("signal 1 : " + asd);
        }

        for (double dsa : signal2) {
            System.out.println("signal 2 : " + dsa);
        }

        System.out.println("MSE: " + mse );

        if (mse <= threshold) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean alchemy(String audio1, byte[] audio2) {

        double[] signal1 = MFCC.compute(FourierTransform.Spectrum(nextPowOfTwo(hamming(toDoubleArray(readAudioFile(audio1))))));
        double[] signal2 = MFCC.compute(FourierTransform.Spectrum(nextPowOfTwo(hamming(toDoubleArray(audio2)))));

        double mse = Utilities.MSE(signal1, signal2, signal1.length);

        System.out.println("MSE: " + mse );

        if (mse <= threshold) {
            return true;
        } else {
            return false;
        }
    }

    // converts byte array to double array
    private static double[] toDoubleArray(byte[] byteArray){
        int SAMPLE_RESOLUTION = 16;
        int BYTES_PER_POINT = SAMPLE_RESOLUTION / 8;
        int[] vals = new int[byteArray.length/BYTES_PER_POINT];
        double[] Ys = new double[byteArray.length / BYTES_PER_POINT];
        double[] Xs = new double[byteArray.length / BYTES_PER_POINT];
        double[] Xs2 = new double[byteArray.length / BYTES_PER_POINT];
        byte hByte;
        byte lByte;

        for (int i=0; i<vals.length; i++)
        {
            // bit shift the byte buffer into the right variable format
            hByte = byteArray[i * 2 + 1];
            lByte = byteArray[i * 2 + 0];
            vals[i] = (int)(short)((hByte << 8) | lByte);
            Xs[i] = i;
            Ys[i] = vals[i];
            Xs2[i] = (double)i/Ys.length*8000/1000.0; // units are in kHz
        }

        return Ys;
    }

    // reads raw audio file into byte array
    private static byte[] readAudioFile(String audioFileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + audioFileName + ".pcm");
        byte[] byteData = new byte[(int) file.length()];

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(byteData);
        } catch (FileNotFoundException e ) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//
//        try {
//            FileInputStream in = new FileInputStream(file);
//            in.read(byteData);
//            in.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return byteData;

    }

    public static double[] toDouble(byte[] a)
    {
        double[] y = new double[a.length];

        for(int x = 0; x < y.length; x++)
            y[x] = (double)a[x];

        return y;
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
        //System.out.println("nextpowoftwo: " + nextpowtwo);
        return sampleBuffer;
    }

    private static double[] hamming(double[] signal) {

        double [] newSignal = new double[signal.length];
        int samples = signal.length / 2;
        double r = Math.PI / samples;

        for (int i = -samples; i<samples; i++) {
            newSignal[samples+ i] = 0.54 + 0.46 * Math.cos(i * r);
        }

        return newSignal;
    }

    private static double[] hanning(double[] signal) {

        double[] newSignal = new double[signal.length];
        for (int i = 0; i < signal.length; i++) {
            double multiplier = 0.5 * (1 - Math.cos(2*Math.PI*i/signal.length-1));
            newSignal[i] = multiplier * signal[i];
        }
        return newSignal;
    }

}
