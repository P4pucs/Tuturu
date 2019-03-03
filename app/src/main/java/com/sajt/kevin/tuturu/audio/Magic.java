package com.sajt.kevin.tuturu.audio;

import android.os.Environment;

import com.sajt.kevin.tuturu.math.DSP.Filters;
import com.sajt.kevin.tuturu.math.DSP.FourierTransform;
import com.sajt.kevin.tuturu.math.DSP.MFCC;
import com.sajt.kevin.tuturu.math.DSP.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Magic {

    public static boolean magic(String audio1, String audio2) {
// threading for later
//        new Thread(() -> {
//            double[] signal1 = compressor(FFT.fftMagnitude(nextPowOfTwo(Corr.toDouble(readAudioFile(audio1)))), 10);
//            System.out.println("DONE 1111111111" + " size: " + signal1.length);
//        }).start();
//
//        new Thread(() -> {
//            double[] signal2 = compressor(FFT.fftMagnitude(nextPowOfTwo(Corr.toDouble(readAudioFile(audio2)))), 10);
//            System.out.println("DONE 2222222222" + " size: " + signal2.length);
//        }).start();
//
//        thread.start();//0.019919727065442257

        double[] signal1 = MFCC.compute(FourierTransform.Spectrum(nextPowOfTwo(hamming(toDouble(readAudioFile(audio1))))));
        double[] signal2 = MFCC.compute(FourierTransform.Spectrum(nextPowOfTwo(hamming(toDouble(readAudioFile(audio2))))));

        double mse = Utilities.MSE(signal1, signal2, signal1.length);

        System.out.println("MSE: " + mse );

        if (mse <= 0.03) { //0.009045041910
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
            Xs2[i] = (double)i/Ys.length*44100/1000.0; // units are in kHz
        }

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

    private static double[] reverse(double[] array) {
        for(int i = 0; i < array.length / 2; i++)
        {
            double temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
        return array;
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

}
