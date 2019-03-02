package com.sajt.kevin.tuturu.audio;

import android.os.Environment;

import com.sajt.kevin.tuturu.math.DSP;
import com.sajt.kevin.tuturu.math.DSPC;
import com.sajt.kevin.tuturu.math.FFT;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Magic {

    public static void magic(String audio1, String audio2) {
// threading for later
//        new Thread(() -> {
//            double[] signal1 = compressor(FFT.fftMagnitude(nextPowOfTwo(DSPC.todouble(readAudioFile(audio1)))), 10);
//            System.out.println("DONE 1111111111" + " size: " + signal1.length);
//        }).start();
//
//        new Thread(() -> {
//            double[] signal2 = compressor(FFT.fftMagnitude(nextPowOfTwo(DSPC.todouble(readAudioFile(audio2)))), 10);
//            System.out.println("DONE 2222222222" + " size: " + signal2.length);
//        }).start();
//        double[] sajt1 = FFT.fftMagnitude(nextPowOfTwo(DSPC.todouble(readAudioFile(audio1))));
//        double[] sajt2 = FFT.fftMagnitude(nextPowOfTwo(DSPC.todouble(readAudioFile(audio2))));


//        int sajt = 5000;
//
//        double[] sajt3 = new double[sajt];
//        double[] sajt4 = new double[sajt];
//        for (int i=0;i<sajt;i++) {
//            sajt3[i] = sajt1[i];
//            sajt4[i] = sajt2[i];
//        }
//
//        double[] krumpli = DSPC.xcorr(sajt3, sajt4);
////            for (double asd : krumpli) {
////                System.out.println("krumplisteszta: " + asd);
////            }
//
//            double max = DSPC.max(krumpli);
//            double min = DSPC.min(krumpli);
//            System.out.println("krumplisteszta MAX: " + max);
//            System.out.println("krumplisteszta min: " + min);

//        Thread thread = new Thread(() -> {
//            System.out.println("krumplisteszta thread started");
//            double[] alma = DSPC.xcorr(sajt1, sajt2);
//
//            for (double krumpli : alma) {
//                System.out.println("krumplisteszta: " + krumpli);
//            }
//        });
//        thread.start();


        long startTime = System.currentTimeMillis();

        double[] signal1 = DSP.mfcc(FFT.fftMagnitude(nextPowOfTwo(hamming(filter(DSPC.todouble(readAudioFile(audio1)))))));
        double[] signal2 = DSP.mfcc(FFT.fftMagnitude(nextPowOfTwo(hamming(filter(DSPC.todouble(readAudioFile(audio2)))))));
        double[] xcorr = DSPC.xcorr(signal1, signal2);

        //double mse = DSP.MSE(signal1, signal2, signal1.length);


        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;

        System.out.println("max: " + DSPC.max(xcorr) + " min: " + DSPC.min(xcorr) + " elapsed time: " + elapsedTime);

        for (double asd : signal1) {
            System.out.println("signal1: " + asd);
        }

        for (double dsa : signal2) {
            System.out.println("signal2: " + dsa);
        }

        for (double qwe : xcorr) {
            System.out.println("xcorr: " + qwe);
        }
        //System.out.println("MSE: " + mse );
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

    private static double[]filter(double[] signal) {
        double[] magicYs = new double[signal.length];
        for (int i=0; i<magicYs.length-1; i++)
        {
            magicYs[i] = signal[i+1] - 0.95 * signal[i];
        }
        return magicYs;
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

    private static double[] compressor(double[] array, int ratio) {
        double[] newArray = new double[array.length/ratio];
        int index = 0;
        for (int i=0;i<array.length;i+=ratio) {
            newArray[index] = array[i];
        }
        return newArray;
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
