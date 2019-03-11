package com.sajt.kevin.tuturu.math.DSP;

public class MFCC {
    private static double[] melWorkingFrequencies = new double[] { 10.0, 20.0, 90.0, 300.0, 680.0, 1270.0, 2030.0, 2970.0, 4050.0, 5250.0, 6570.0 };

    public static int numMelFilters(int Nyquist)
    {
        //System.Diagnostics.Debug.WriteLine("Nyquist:" + Convert.ToString(Nyquist));
        double frequency = Nyquist;
        double delta = mel(frequency);

        int numFilters = 0;

        while (frequency > 10)
        {
            ++numFilters;

            frequency -= (delta / 2);
            delta = MFCC.mel(frequency);

            //System.Diagnostics.Debug.WriteLine("Frequency:" + Convert.ToString(frequency));
        }

        return numFilters;
    }

    /// <summary>
    /// Init
    /// </summary>
    /// <param name="sampleSize"></param>
    ///
    public static void initMelFrequenciesRange(int Nyquist)
    {
        //System.Diagnostics.Debug.WriteLine("Nyquist:" + Convert.ToString(Nyquist));
        double frequency = Nyquist;
        double delta = mel(frequency);

        int numFilters = numMelFilters(Nyquist);

        melWorkingFrequencies = new double[numFilters];

        frequency = Nyquist;
        delta = mel(frequency);
        int i = 0;
        double cFreq = 0;

        while (frequency > 10)
        {
            frequency -= (delta / 2);
            delta = mel(frequency);
            cFreq = Math.round(frequency);
            //melWorkingFrequencies[numFilters] = Math.Round(frequency);
            melWorkingFrequencies[numFilters-1-i] = 10;
            while (melWorkingFrequencies[numFilters-1-i] < (cFreq - 10)) melWorkingFrequencies[numFilters-1-i] += 10;

            // System.Diagnostics.Debug.WriteLine("Frequency:" + Convert.ToString(melWorkingFrequencies[numFilters-1-i]));
            ++i;
        }

    }

    public static double[] compute(double[] signal)
    {
        System.out.println("RÁÁÁÁÁK: " + melWorkingFrequencies.length + " / " + signal.length);

        double[] result = new double[melWorkingFrequencies.length];
        double[] mfcc = new double[melWorkingFrequencies.length];

        int segment = 0;
        int start = 0;
        int end = 0;

        for (int i = 0; i < melWorkingFrequencies.length; i++)
        {
            result[i] = 0;
            segment = (int) Math.round(mel( melWorkingFrequencies[i] ) / 10 );
                /*System.Diagnostics.Debug.WriteLine("slot #" + Convert.ToString(i));
                System.Diagnostics.Debug.WriteLine("freq:" + Convert.ToString(melWorkingFrequencies[i]));
                System.Diagnostics.Debug.WriteLine("mel:" + Convert.ToString(mel(melWorkingFrequencies[i])));
                System.Diagnostics.Debug.WriteLine("segment:"+Convert.ToString(segment));*/

            start = (segment - (int)Math.floor(segment / 2));
            end = (segment + (segment / 2));
            //System.Diagnostics.Debug.WriteLine("\tstart:" + Convert.ToString(start) + "\tend:" + Convert.ToString(end));

            for (int j = start; j < end; j++)
            {
                // System.Diagnostics.Debug.WriteLine("\t\tfilter slopet:" + Convert.ToString(DSP.Filters.Triangular(j-start, segment)));
                result[i] += signal[j] * Filters.Triangular(j, segment);
            }
            //System.Diagnostics.Debug.WriteLine("result[i]:" + Convert.ToString(result[i]));
            result[i] = (result[i] > 0)  ? Math.log10( Math.abs(result[i]) ) : 0;
        }

        for (int i = 0; i < melWorkingFrequencies.length; i++)
        {
            for (int j = 0; j < melWorkingFrequencies.length; j++)
            {
                mfcc[i] += result[i] * Math.cos(((Math.PI * i) / melWorkingFrequencies.length) * (j - 0.5));
            }
            mfcc[i] *= Math.sqrt(2.0 / (double) melWorkingFrequencies.length);
            //System.Diagnostics.Debug.WriteLine("result[i]:" + Convert.ToString(mfcc[i]));
        }

        return mfcc;
    }

    public static double mel(double value)
    {
        return (2595.0 * (double)Math.log10(1.0 + value / 700.0));
    }

    public static double melinv(double value)
    {
        return (700.0 * ((double)Math.pow(10.0, value / 2595.0) - 1.0));
    }
}
