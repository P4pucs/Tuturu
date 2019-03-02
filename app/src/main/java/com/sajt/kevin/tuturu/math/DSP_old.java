package com.sajt.kevin.tuturu.math;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.DecimalFormat;

public class DSP_old {

    public static int RAW = 1;
    public static int DECIBEL = 2;
    private static double[] melWorkingFrequencies = new double[]{ 10.0, 20.0, 90.0, 300.0, 680.0, 1270.0, 2030.0, 2970.0, 4050.0, 5250.0, 6570.0 };

    public static double[] Spectrum(double[] x) {
        int method = RAW;
        //int pow2Samples = FFT.NextPowerOfTwo((int)x.Length);
        double[] xre = new double[x.length];
        double[] xim = new double[x.length];
        double xreValue;
        double ximValue;
        Long xreLongBits;
        Long ximLongBits;

        Compute(x.length, x, null, xre, xim);

        double[] decibel = new double[xre.length / 2];

        xre[0] = xre[1];
        xim[0] = xim[1];

        for (int i = 0; i < decibel.length; i++) {

            xreLongBits = Double.doubleToLongBits(xre[i]);
            ximLongBits = Double.doubleToLongBits(xim[i]);

            xreValue = Double.longBitsToDouble(xreLongBits * xreLongBits);
            ximValue = Double.longBitsToDouble(ximLongBits * ximLongBits);

            if (method == DECIBEL) {
                decibel[i] = 10.0 * Math.log10((Math.sqrt(xreValue + ximValue)));
            } else {
                decibel[i] = (Math.sqrt(xreValue + ximValue));
            }
        }

        return decibel;
    }

    public static void Compute(int NumSamples, double[] pRealIn, double[] pImagIn,
                               double[] pRealOut, double[] pImagOut) {

        int NumBits;    /* Number of bits needed to store indices */
        int i, j, k, n;
        int BlockSize, BlockEnd;

        double angle_numerator = 2.0 * Math.PI;
        double tr, ti;     /* temp real, temp imaginary */

        if (pRealIn == null || pRealOut == null || pImagOut == null) {
            // error
            throw new RuntimeException("Null argument");
        }
        if (!IsPowerOfTwo(NumSamples)) {
            // error
            throw new RuntimeException("Number of samples must be power of 2");
        }
        if (pRealIn.length < NumSamples || (pImagIn != null && pImagIn.length < NumSamples) ||
                pRealOut.length < NumSamples || pImagOut.length < NumSamples)  {
            // error
            throw new RuntimeException("Invalid Array argument detected");
        }

        NumBits = NumberOfBitsNeeded(NumSamples);

        /*
         **   Do simultaneous data copy and bit-reversal ordering into outputs...
         */
        for (i = 0; i < NumSamples; i++) {
            j = ReverseBits(i, NumBits);
            pRealOut[j] = pRealIn[i];
            pImagOut[j] = (pImagIn == null) ? 0.0 : pImagIn[i];
        }

        /*
         **   Do the FFT itself...
         */
        BlockEnd = 1;
        for (BlockSize = 2; BlockSize <= NumSamples; BlockSize <<= 1) {

            double delta_angle = angle_numerator / (double)BlockSize;
            double sm2 = Math.sin(-2 * delta_angle);
            double sm1 = Math.sin(-delta_angle);
            double cm2 = Math.cos(-2 * delta_angle);
            double cm1 = Math.cos(-delta_angle);
            double w = 2 * cm1;
            double ar0, ar1, ar2;
            double ai0, ai1, ai2;

            for (i = 0; i < NumSamples; i += BlockSize) {
                ar2 = cm2;
                ar1 = cm1;

                ai2 = sm2;
                ai1 = sm1;

                for (j = i, n = 0; n < BlockEnd; j++, n++) {
                    ar0 = w * ar1 - ar2;
                    ar2 = ar1;
                    ar1 = ar0;

                    ai0 = w * ai1 - ai2;
                    ai2 = ai1;
                    ai1 = ai0;

                    k = j + BlockEnd;
                    tr = ar0 * pRealOut[k] - ai0 * pImagOut[k];
                    ti = ar0 * pImagOut[k] + ai0 * pRealOut[k];

                    pRealOut[k] = (pRealOut[j] - tr);
                    pImagOut[k] = (pImagOut[j] - ti);

                    pRealOut[j] += (tr);
                    pImagOut[j] += (ti);
                }
            }
            BlockEnd = BlockSize;
        }
    }

    public static int NumberOfBitsNeeded(int PowerOfTwo) {
        if (PowerOfTwo > 0) {
            for (int i = 0, mask = 1; ; i++, mask <<= 1) {
                if ((PowerOfTwo & mask) != 0)
                    return i;
            }
        }
        return 0; // error
    }

    public static boolean IsPowerOfTwo(int x) {
        return ((x != 0) && (x & (x - 1)) == 0);
    }

    public static int ReverseBits(int index, int NumBits) {
        int i, rev;

        for (i = rev = 0; i < NumBits; i++) {
            rev = (rev << 1) | (index & 1);
            index >>= 1;
        }

        return rev;
    }

    public static double[] mfcc(double[] signal) {

        double[] result = new double[melWorkingFrequencies.length];
        double[] mfcc = new double[melWorkingFrequencies.length];

        int segment = 0;
        int start = 0;
        int end = 0;

        for (int i = 0; i < melWorkingFrequencies.length; i++) {

            result[i] = 0;
            segment = (int) Math.round(mel( melWorkingFrequencies[i] ) / 10 );
                /*System.Diagnostics.Debug.WriteLine("slot #" + Convert.ToString(i));
                System.Diagnostics.Debug.WriteLine("freq:" + Convert.ToString(melWorkingFrequencies[i]));
                System.Diagnostics.Debug.WriteLine("mel:" + Convert.ToString(mel(melWorkingFrequencies[i])));
                System.Diagnostics.Debug.WriteLine("segment:"+Convert.ToString(segment));*/

            start = (segment - (int)Math.floor(segment / 2));
            end = (segment + (segment / 2));
            //System.Diagnostics.Debug.WriteLine("\tstart:" + Convert.ToString(start) + "\tend:" + Convert.ToString(end));

            for (int j = start; j < end; j++) {
                // System.Diagnostics.Debug.WriteLine("\t\tfilter slopet:" + Convert.ToString(DSP.Filters.Triangular(j-start, segment)));
                result[i] += signal[j] * Triangular(j, segment);
            }
            //System.Diagnostics.Debug.WriteLine("result[i]:" + Convert.ToString(result[i]));
            result[i] = (result[i] > 0)  ? Math.log10( Math.abs(result[i]) ) : 0;
        }

        for (int i = 0; i < melWorkingFrequencies.length; i++) {
            for (int j = 0; j < melWorkingFrequencies.length; j++) {
                mfcc[i] += result[i] * Math.cos(((Math.PI * i) / melWorkingFrequencies.length) * (j - 0.5));
            }
            mfcc[i] *= Math.sqrt(2.0 / (double) melWorkingFrequencies.length);
            //System.Diagnostics.Debug.WriteLine("result[i]:" + Convert.ToString(mfcc[i]));
        }

        return mfcc;
    }

    public static double mel(double value) {
        return (2595.0 * Math.log10(1.0 + value / 700.0));
    }

    public static double Triangular(double value, double samples) {
        return (2 / (samples + 1)) * (((samples + 1) / 2) - Math.abs(value - ((samples - 1) / 2)));
    }

    public static double MSE(double[] signal_1, double[] signal_2, int SizeToCompare) {
        double result = 0;

        if (signal_1.length < SizeToCompare) return -1;
        if (signal_2.length < SizeToCompare) return -1;

        for (int i = 0; i < signal_1.length; i++) {
            result += Math.pow(signal_1[i] - signal_2[i], 2);
        }
        return (result / signal_1.length);
    }
}
