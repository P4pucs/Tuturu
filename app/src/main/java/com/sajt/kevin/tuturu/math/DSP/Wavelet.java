package com.sajt.kevin.tuturu.math.DSP;

public class Wavelet {
    // Haar Wavelet

    /// <summary>
    /// Calculate Haar in-place forward fast wavelet transform in 1-dimension.
    /// </summary>
    /// <param name="n">Elements to compute</param>
    /// <param name="signal">Signal to compute</param>
    static void Haar_ip_FFWT_1d(int n, double[] signal)
    {
        double a;
        double c;
        int i; int j; int k; int l; int m;

        i = 1;
        j = 2;

        m = (int) Utilities.NextPowerOfTwo( (int) n );

        for (l = n - 1; l >= 0; l--)
        {
            //System.Diagnostics.Debug.WriteLine("l = " + l);
            m /= 2;

            for (k = 0; k < m; k++)
            {
                a = (signal[j * k] + signal[j * k + i]) / 2.0;
                c = (signal[j * k] - signal[j * k + i]) / 2.0;
                signal[j * k] = a;
                signal[j * k + i] = c;
            }

            i *= 2;
            j *= 2;
        }
    }

    /// <summary>
    /// Calculate Haar in-place inverse fast wavelet transform in 1-dimension.
    /// </summary>
    /// <param name="n">Elements to compute</param>
    /// <param name="signal">Signal to compute (must be power two)</param>
    static void Haar_ip_IFWT_1d(int n, double[] signal)
    {
        double a0;
        double a1;
        int i;
        int j;
        int k;
        int l;
        int m;

        i = (int) Utilities.NextPowerOfTwo((int) (n - 1) );
        j = 2 * i;
        m = 1;

        for (l = 1; l <= n; l++)
        {
            //System.Diagnostics.Debug.WriteLine("l = " + l);

            for (k = 0; k < m; k++)
            {
                a0 = signal[j * k] + signal[j * k + i];
                a1 = signal[j * k] - signal[j * k + i];
                signal[j * k] = a0;
                signal[j * k + i] = a1;
            }

            i /= 2;
            j /= 2;
            m *= 2;
        }
    }

    /// <summary>
    /// Calculate one iteration of the Haar forward FWT in 1-dimension.
    /// </summary>
    /// <param name="n">Elements to compute</param>
    /// <param name="signal">Signal to compute (must be power two)</param>
    static void Haar_forward_pass_1d(int n, double[] signal)
    {
        int i;
        int npts;

        npts = (int) Utilities.NextPowerOfTwo( (int) n );
        double[] a = new double[ npts / 2 ];
        double[] c = new double[ npts / 2 ];

        for (i = 0; i < npts / 2; i++)
        {
            a[i] = (signal[2 * i] + signal[2 * i + 1]) / 2.0;
            c[i] = (signal[2 * i] - signal[2 * i + 1]) / 2.0;
        }

        for (i = 0; i < npts / 2; i++)
        {
            signal[i] = a[i];
            signal[i + npts / 2] = c[i];
        }

    }

    /// <summary>
    /// Calculate the Haar forward fast wavelet transform in 1-dimension.
    /// </summary>
    /// <param name="signal">Signal to compute (must be power two)</param>
    public static void Haar_forward_FWT_1d(double[] signal)
    {
        int m;
        int npts;

        npts = (int) Utilities.NextPowerOfTwo((int) signal.length);

        for (m = signal.length - 1; m >= 0; m--)
        {
            Wavelet.Haar_forward_pass_1d(m + 1, signal);
            //Wavelet.Haar_ip_FFWT_1d(m + 1, ref signal);
        }

            /*int i = 0;
            int w = signal.Length;
            double[] vecp = new double[signal.Length];
            for (i = 0; i < signal.Length; i++)
                vecp[i] = 0;

            //while (w > 1)
           // {
                w /= 2;
                for (i = 0; i < w; i++)
                {
                    vecp[i] = (signal[2 * i] + signal[2 * i + 1]) / Math.Sqrt(2.0);
                    vecp[i + w] = (signal[2 * i] - signal[2 * i + 1]) / Math.Sqrt(2.0);
                }

                for (i = 0; i < (w * 2); i++)
                    signal[i] = vecp[i];
           // }*/

    }



    /// <summary>
    /// Calculate one iteration of the Haar inverse FWT in 1-dimension.
    /// </summary>
    /// <param name="n">Elements to compute</param>
    /// <param name="signal">Signal to compute (must be power two)</param>
    static void Haar_inverse_pass_1d(int n, double[] signal)
    {
        int i;
        int npts = (int)  Utilities.NextPowerOfTwo( (int) n);
        double[] r = new double[npts];

        for (i = 0; i < npts / 2; i++)
        {
            r[2 * i] = signal[i] + signal[i + npts / 2];
            r[2 * i + 1] = signal[i] - signal[i + npts / 2];
        }

        for (i = 0; i < npts; i++)
        {
            signal[i] = r[i];
        }
    }


    /// <summary>
    /// Calculate the Haar inverse fast wavelet transform in 1-dimension.
    /// </summary>
    /// <param name="signal">Signal to compute (must be power two)</param>
    public static void Haar_inverse_FWT_1d(double[] signal)
    {
        for (int m = 2; m <= signal.length; m++)
        {
            Wavelet.Haar_inverse_pass_1d(m, signal);
        }
    }


    /// <summary>
    /// Calculate one iteration of the Haar forward FWT in 2-dimensions.
    /// </summary>
    /// <param name="n">Elements to compute</param>
    /// <param name="signal">Signal to compute (must be power two)</param>
       /* static void Haar_forward_pass_2d(int n, ref double[][] signal)
        {
            int i, j;
            int npts;

            npts = (int) Utilities.NextPowerOfTwo( (uint) n);

            for (i = 0; i < npts; i++)
            {
                Wavelet.Haar_forward_pass_1d(n, ref signal[i]);
            }

            double[] c = new double [npts];

            for (j = 0; j < npts; j++)
            {
                for (i = 0; i < npts; i++)
                    c[i] = signal[i][j];
                Wavelet.Haar_forward_pass_1d(n, ref c);
                for (i = 0; i < npts; i++)
                    signal[i][j] = c[i];
            }
        }*/

    /// <summary>
    /// Calculate the Haar forward fast wavelet transform in 2-dimensions.
    /// </summary>
    /// <param name="n">Elements to compute</param>
    /// <param name="signal">Signal to compute (must be power two)</param>
    public static void Haar_forward_FWT_2d(int width, int height, double[] signal)
    {
            /*for (int m = n - 1; m >= 0; m--)
            {
                Haar_forward_pass_2d(m + 1, ref signal);
            }*/

        double[] row    = new double[Utilities.NextPowerOfTwo( (int) width)];
        double[] column = new double[Utilities.NextPowerOfTwo( (int) height)];

        for (int y = 0; y < height; y++)
        {
            System.arraycopy(signal, (int)(y * width), row, 0, (int)width);
            Wavelet.Haar_forward_FWT_1d(row);
            System.arraycopy(row, 0, signal, (int)(y * width), (int)width);
        }

        for (int x = 0; x < width; x++)
        {

            for (int y = 0; y < height; y++)
            {
                column[y] = signal[x + (y * width)];
            }

            Wavelet.Haar_forward_FWT_1d(column);

            for (int y = 0; y < height; y++)
            {
                signal[x + (y * width)] = column[y];
            }

        }

    }

    /// <summary>
    /// Calculate one iteration of the Haar inverse FWT in 2-dimensions.
    /// </summary>
    /// <param name="signal">Signal to compute (must be power two)</param>
       /* static void Haar_inverse_pass_2d(int n, ref double[][] signal)
        {
            int i, j;
            int npts;

            npts = (int) Utilities.NextPowerOfTwo((uint) n);

            for (i = 0; i < npts; i++)
            {
                Wavelet.Haar_inverse_pass_1d(n, ref signal[i]);
            }

            double[] c = new double[npts];

            for (j = 0; j < npts; j++)
            {
                for (i = 0; i < npts; i++)
                    c[i] = signal[i][j];

                Wavelet.Haar_inverse_pass_1d(n, ref c);

                for (i = 0; i < npts; i++)
                    signal[i][j] = c[i];
            }
        }*/


    /// <summary>
    /// Calculate the Haar inverse fast wavelet transform in 2-dimensions.
    /// </summary>
    /// <param name="signal">Signal to compute (must be power two)</param>
    public static void Haar_inverse_FWT_2d(int width, int height, double[] signal)
    {
            /*for (int m = 1; m <= signal.Length; m++)
            {
                Haar_inverse_pass_2d(m, ref signal);
            }*/

        double[] row = new double[Utilities.NextPowerOfTwo((int)width)];
        double[] column = new double[Utilities.NextPowerOfTwo((int)height)];

        for (int x = 0; x < width; x++)
        {

            for (int y = 0; y < height; y++)
            {
                column[y] = signal[x + (y * width)];
            }

            Wavelet.Haar_inverse_FWT_1d(column);

            for (int y = 0; y < height; y++)
            {
                signal[x + (y * width)] = column[y];
            }

        }

        for (int y = 0; y < height; y++)
        {
            System.arraycopy(signal, (int)(y * width), row, 0, (int)width);
            Wavelet.Haar_inverse_FWT_1d(row);
            System.arraycopy(row, 0, signal, (int)(y * width), (int)width);
        }
    }

    // The 1D Haar Transform
    public static void haar1d(double[] vec, int n)
    {
        int i = 0;
        int w = n;
        double[] vecp = new double[n];
        for (i = 0; i < n; i++)
            vecp[i] = 0;

        while (w > 1)
        {
            w /= 2;
            for (i = 0; i < w; i++)
            {
                vecp[i] = (vec[2 * i] + vec[2 * i + 1]) / Math.sqrt(2.0);
                vecp[i + w] = (vec[2 * i] - vec[2 * i + 1]) / Math.sqrt(2.0);
            }

            for (i = 0; i < (w * 2); i++)
                vec[i] = vecp[i];
        }

    }

    // A Modified version of 1D Haar Transform, used by the 2D Haar Transform function
    static void haar1(double[] vec, int n, int w)
    {
        int i = 0;
        double[] vecp = new double[n];
        for (i = 0; i < n; i++)
            vecp[i] = 0;

        w /= 2;
        for (i = 0; i < w; i++)
        {
            vecp[i] = (vec[2 * i] + vec[2 * i + 1]) / Math.sqrt(2.0);
            vecp[i + w] = (vec[2 * i] - vec[2 * i + 1]) / Math.sqrt(2.0);
        }

        for (i = 0; i < (w * 2); i++)
            vec[i] = vecp[i];

    }

    // The 2D Haar Transform
    public static void haar2(double[] matrix, int rows, int cols)
    {
        double[] temp_row = new double[cols];
        double[] temp_col = new double[rows];

        int i = 0, j = 0;
        int w = cols, h = rows;
        while (w > 1 || h > 1)
        {
            if (w > 1)
            {
                for (i = 0; i < h; i++)
                {
                    for (j = 0; j < cols; j++)
                        temp_row[j] = matrix[i + (j * cols)];

                    haar1(temp_row, cols, w);

                    for (j = 0; j < cols; j++)
                        matrix[i + (j * cols)] = temp_row[j];
                }
            }

            if (h > 1)
            {
                for (i = 0; i < w; i++)
                {
                    for (j = 0; j < rows; j++)
                        temp_col[j] = matrix[j + (i * cols)];
                    haar1(temp_col, rows, h);
                    for (j = 0; j < rows; j++)
                        matrix[j + (i * cols)] = temp_col[j];
                }
            }

            if (w > 1)
                w /= 2;
            if (h > 1)
                h /= 2;
        }

    }
}
