package com.sajt.kevin.tuturu.math.DSP;

public class Utilities {
    public static Double DDC_PI = 3.14159265358979323846;

    public static double MSE(double[] signal_1, double[] signal_2, int SizeToCompare)
    {
        double result = 0;

        if (signal_1.length < SizeToCompare) return -1;
        if (signal_2.length < SizeToCompare) return -1;

        for (int i = 0; i < signal_1.length; i++)
        {
            result += Math.pow(signal_1[i] - signal_2[i], 2);
        }

        return (result / signal_1.length);
    }

    /// <summary>
    /// Verifies a number is a power of two
    /// </summary>
    /// <param name="x">Number to check</param>
    /// <returns>true if number is a power two (i.e.:1,2,4,8,16,...)</returns>
    public static Boolean IsPowerOfTwo(int x)
    {
        return ((x != 0) && (x & (x - 1)) == 0);
    }

    /// <summary>
    /// Get Next power of number.
    /// </summary>
    /// <param name="x">Number to check</param>
    /// <returns>A power of two number</returns>
    public static int NextPowerOfTwo(int x)
    {
        x = x - 1;
        x = x | (x >> 1);
        x = x | (x >> 2);
        x = x | (x >> 4);
        x = x | (x >> 8);
        x = x | (x >> 16);
        return x + 1;
    }

    public static double[] triangularExtraction(double[] value, int width, int height, int num) { //fill =-1
        int fill = -1;
        double[] result = new double[num*2];
        int sidew = (int) Math.round(Math.sqrt(num*2));
        if (sidew > height) sidew = height;
        int sideh = sidew;
        int index = 0;
        String _match = "";
        for (int y = 0; y < sideh; y++)
        {
            for (int x = 0; x < sidew; x++)
            {
                result[index] = value[x + (y * width)];
                _match += "[" + Math.round(result[index]) + "],";
                if (fill != -1)
                {
                    value[x + (y * width)] = fill;
                    value[width-1-x + (y * width)] = fill;
                    value[x + ((height - y -1 ) * width)] = fill;
                    value[width - 1 - x + ((height - y - 1) * width)] = fill;
                }
                index++;
                //if (index >= num) break;
            }
            --sidew;
        }
        return result;
    }

}
