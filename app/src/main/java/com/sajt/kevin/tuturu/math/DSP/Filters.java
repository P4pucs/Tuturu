package com.sajt.kevin.tuturu.math.DSP;

public class Filters {
    public static double Triangular(double value, double samples)
    {
        return (2 / (samples + 1)) * (((samples + 1) / 2) - Math.abs(value - ((samples - 1) / 2)));
    }

    public static double[] EnhanceHighFrequencies(double[] signal)
    {
        double[] result = new double[signal.length];

        for (int i = 1; i < signal.length; i++)
        {
            result[i] = signal[i] - 0.90 * signal[i - 1]; //0.95 originally
        }

        return result;
    }
}
