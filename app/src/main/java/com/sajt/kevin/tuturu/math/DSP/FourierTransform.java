package com.sajt.kevin.tuturu.math.DSP;

public class FourierTransform {

    public static int Raw = 1;
    public static int Decibel = 2;

    public static int FREQUENCYSLOTCOUNT = 21;
    private static int[] _meterFrequencies = new int[]{20, 30, 55, 80, 120, 155, 195, 250, 375, 500, 750, 1000, 1500, 2000, 3000, 4000, 6000, 8000, 12000, 16000, 20000};
    private static int _frequencySlotCount = FREQUENCYSLOTCOUNT;

    /// <summary>
    /// 	Changes the Frequency Bands to analyze.
    /// 	Affects the other static methods
    /// </summary>
    /// <param name="meterFrequencies"></param>
    public static void SetMeterFrequencies(int[] meterFrequencies) {
        _meterFrequencies = meterFrequencies;
        _frequencySlotCount = meterFrequencies.length;
    }

    public static double[] Spectrum(double[] x) {
        int method = Raw;
        //int pow2Samples = FFT.NextPowerOfTwo((int)x.Length);
        double[] xre = new double[x.length];
        double[] xim = new double[x.length];

        Compute(x.length, x, null, xre, xim, false);

        double[] decibel = new double[xre.length / 2];

        for (int i = 0; i < decibel.length; i++)
            decibel[i] = (method == Decibel) ? 10.0 * Math.log10((float) (Math.sqrt((xre[i] * xre[i]) + (xim[i] * xim[i])))) : (float) (Math.sqrt((xre[i] * xre[i]) + (xim[i] * xim[i])));
        return decibel;
    }


    /// <summary>
    /// Get Number of bits needed for a power of two
    /// </summary>
    /// <param name="PowerOfTwo">Power of two number</param>
    /// <returns>Number of bits</returns>
    public static int NumberOfBitsNeeded(int PowerOfTwo) {
        if (PowerOfTwo > 0) {
            for (int i = 0, mask = 1; ; i++, mask <<= 1) {
                if ((PowerOfTwo & mask) != 0)
                    return i;
            }
        }
        return 0; // error
    }

    /// <summary>
    /// Reverse bits
    /// </summary>
    /// <param name="index">Bits</param>
    /// <param name="NumBits">Number of bits to reverse</param>
    /// <returns>Reverse Bits</returns>
    public static int ReverseBits(int index, int NumBits) {
        int i, rev;

        for (i = rev = 0; i < NumBits; i++) {
            rev = (rev << 1) | (index & 1);
            index >>= 1;
        }

        return rev;
    }

    /// <summary>
    /// Return index to frequency based on number of samples
    /// </summary>
    /// <param name="Index">sample index</param>
    /// <param name="NumSamples">number of samples</param>
    /// <returns>Frequency index range</returns>
    public static Double IndexToFrequency(int Index, int NumSamples) {
        if (Index >= NumSamples)
            return 0.0;
        else if (Index <= NumSamples * 0.5)
            return (double) Index / (double) NumSamples;

        return -(double) (NumSamples - Index) / (double) NumSamples;
    }

    /// <summary>
    /// Compute FFT
    /// </summary>
    /// <param name="NumSamples">NumSamples Number of samples (must be power two)</param>
    /// <param name="pRealIn">Real samples</param>
    /// <param name="pImagIn">Imaginary (optional, may be null)</param>
    /// <param name="pRealOut">Real coefficient output</param>
    /// <param name="pImagOut">Imaginary coefficient output</param>
    /// <param name="bInverseTransform">bInverseTransform when true, compute Inverse FFT</param>
    public static void Compute(int NumSamples, double[] pRealIn, double[] pImagIn,
                               double[] pRealOut, double[] pImagOut, boolean bInverseTransform) {
        int NumBits;    /* Number of bits needed to store indices */
        int i, j, k, n;
        int BlockSize, BlockEnd;

        double angle_numerator = 2.0 * Math.PI;
        double tr, ti;     /* temp real, temp imaginary */

        if (pRealIn == null || pRealOut == null || pImagOut == null) {
            // error
            throw new NullPointerException("Null argument");
        }
        if (!Utilities.IsPowerOfTwo(NumSamples)) {
            // error
            new Exception("Number of samples must be power of 2");
        }
        if (pRealIn.length < NumSamples || (pImagIn != null && pImagIn.length < NumSamples) ||
                pRealOut.length < NumSamples || pImagOut.length < NumSamples) {
            // error
            new Exception("Invalid Array argument detected");
        }

        if (bInverseTransform)
            angle_numerator = -angle_numerator;

        NumBits = NumberOfBitsNeeded(NumSamples);

        /*
         **   Do simultaneous data copy and bit-reversal ordering into outputs...
         */
        for (i = 0; i < NumSamples; i++) {
            j = ReverseBits(i, NumBits);
            pRealOut[j] = pRealIn[i];
            pImagOut[j] = (double) ((pImagIn == null) ? 0.0 : pImagIn[i]);
        }

        /*
         **   Do the FFT itself...
         */
        BlockEnd = 1;
        for (BlockSize = 2; BlockSize <= NumSamples; BlockSize <<= 1) {
            double delta_angle = angle_numerator / (double) BlockSize;
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

        /*
         **   Need to normalize if inverse transform...
         */
        if (bInverseTransform) {
            double denom = (double) (NumSamples);

            for (i = 0; i < NumSamples; i++) {
                pRealOut[i] /= denom;
                pImagOut[i] /= denom;
            }
        }
    }

    /// <summary>
    /// Calculate normal (power spectrum)
    /// </summary>
    /// <param name="NumSamples">Number of sample</param>
    /// <param name="pReal">Real coefficient buffer</param>
    /// <param name="pImag">Imaginary coefficient buffer</param>
    /// <param name="pAmpl">Working buffer to hold amplitude Xps(m) = | X(m)^2 | = Xreal(m)^2  + Ximag(m)^2</param>
    public static void Norm(int NumSamples, Double[] pReal, Double[] pImag, Double[] pAmpl) {
        if (pReal == null || pImag == null || pAmpl == null) {
            // error
            throw new NullPointerException("pReal,pImag,pAmpl");
        }
        if (pReal.length < NumSamples || pImag.length < NumSamples || pAmpl.length < NumSamples) {
            // error
            new Exception("Invalid Array argument detected");
        }

        // Calculate amplitude values in the buffer provided
        for (int i = 0; i < NumSamples; i++) {
            pAmpl[i] = pReal[i] * pReal[i] + pImag[i] * pImag[i];
        }
    }

    public static double normalizeFFTValue(double value) {
        return (value < 0.1 && value > -0.1) ? 0 : value;
    }

    /// <summary>
    /// Compute 2D FFT
    /// </summary>
    /// <param name="width">Width of the Matrix (must be power two)</param>
    /// <param name="height">Height of the Matrix (must be power two)</param>
    /// <param name="pRealIn">Real samples</param>
    /// <param name="pImagIn">Imaginary (optional, may be null)</param>
    /// <param name="pRealOut">Real coefficient output</param>
    /// <param name="pImagOut">Imaginary coefficient output</param>
    /// <param name="bInverseTransform">bInverseTransform when true, compute Inverse FFT</param>
    public static void Compute2D(int width, int height, Double[] pRealIn, Double[] pImagIn, Double[] pRealOut, Double[] pImagOut, Boolean bInverseTransform) {
        double[] row = new double[width];
        double[] column = new double[height];

        double[] irow = new double[width];
        double[] icolumn = new double[height];

        double[] xre = new double[width];
        double[] xim = new double[width];

        if (!bInverseTransform) {
            for (int y = 0; y < height; y++) {
                System.arraycopy(pRealIn, (int) (y * width), row, 0, (int) width);
                FourierTransform.Compute(width, row, null, xre, xim, bInverseTransform);
                System.arraycopy(xre, 0, pRealOut, (int) (y * width), (int) width);
                System.arraycopy(xim, 0, pImagOut, (int) (y * width), (int) width);
            }

            for (int x = 0; x < width; x++) {

                for (int y = 0; y < height; y++) {
                    column[y] = pRealOut[x + (y * width)];
                    icolumn[y] = pImagOut[x + (y * width)];
                }

                FourierTransform.Compute(height, column, icolumn, xre, xim, bInverseTransform);

                for (int y = 0; y < height; y++) {
                    pRealOut[x + (y * width)] = xre[y];
                    pImagOut[x + (y * width)] = xim[y];
                }

            }
        } else {
            for (int x = 0; x < width; x++) {

                for (int y = 0; y < height; y++) {
                    column[y] = pRealIn[x + (y * width)];
                    icolumn[y] = pImagIn[x + (y * width)];
                }

                FourierTransform.Compute(height, column, icolumn, xre, xim, bInverseTransform);

                for (int y = 0; y < height; y++) {
                    pRealOut[x + (y * width)] = xre[y];
                    pImagOut[x + (y * width)] = xim[y];
                }

            }

            for (int y = 0; y < height; y++) {
                System.arraycopy(pRealOut, (int) (y * width), row, 0, (int) width);
                System.arraycopy(pImagOut, (int) (y * width), irow, 0, (int) width);

                FourierTransform.Compute(width, row, irow, xre, xim, bInverseTransform);
                System.arraycopy(xre, 0, pRealOut, (int) (y * width), (int) width);
            }
        }
    }

    /// <summary>
    /// Find Peak frequency in Hz
    /// </summary>
    /// <param name="NumSamples">Number of samples</param>
    /// <param name="pAmpl">Current amplitude</param>
    /// <param name="samplingRate">Sampling rate in samples/second (Hz)</param>
    /// <param name="index">Frequency index</param>
    /// <returns>Peak frequency in Hz</returns>
    public static Double PeakFrequency(int NumSamples, Double[] pAmpl, Double samplingRate, int index) {
        int N = NumSamples >> 1;   // number of positive frequencies. (numSamples/2)

        if (pAmpl == null) {
            // error
            throw new NullPointerException("pAmpl");
        }
        if (pAmpl.length < NumSamples) {
            // error
            new Exception("Invalid Array argument detected");
        }

        double maxAmpl = -1.0;
        double peakFreq = -1.0;
        index = 0;

        for (int i = 0; i < N; i++) {
            if (pAmpl[i] > maxAmpl) {
                maxAmpl = (double) pAmpl[i];
                index = i;
                peakFreq = (double) (i);
            }
        }

        return samplingRate * peakFreq / (double) (NumSamples);
    }

    public static byte[] GetPeaks(double[] leftChannel, double[] rightChannel, int sampleFrequency) {
        byte[] peaks = new byte[_frequencySlotCount];
        byte[] channelPeaks = GetPeaksForChannel(leftChannel, sampleFrequency);

        ComparePeaks(peaks, channelPeaks);
        return peaks;
    }

    private static void ComparePeaks(byte[] overallPeaks, byte[] channelPeaks) {
        for (int i = 0; i < _frequencySlotCount; i++) {
            if (Byte.compare(overallPeaks[i], channelPeaks[i]) >= 0) {
                overallPeaks[i] = overallPeaks[i];
            } else {
                overallPeaks[i] = channelPeaks[i];
            }
            //overallPeaks[i] = MAX(overallPeaks[i], channelPeaks[i]);
        }
    }

    private static byte[] GetPeaksForChannel(double[] normalizedArray, int sampleFrequency) {
        double maxAmpl = (32767.0 * 32767.0);

        byte[] peaks = new byte[_frequencySlotCount];
        // update meter
        int centerFreq = (sampleFrequency / 2);
        byte peak;
        for (int i = 0; i < _frequencySlotCount; ++i) {
            if (_meterFrequencies[i] > centerFreq) {
                peak = 0;
            } else {
                int index = (int) (_meterFrequencies[i] * normalizedArray.length / sampleFrequency);
                peak = (byte) Math.max(0, (17.0 * Math.log10(normalizedArray[index] / maxAmpl)));
            }

            peaks[i] = peak;
        }

        return peaks;
    }
}

