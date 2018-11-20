package com.sajt.kevin.tuturu.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.sajt.kevin.tuturu.math.FFT;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.sajt.kevin.tuturu.math.DSP.*;


public class Recorder {
    private static final String TAG = "VoiceRecord";

    private static final int RECORDER_SAMPLE_RATE = 44100;
    private static final int RECORDER_CHANNELS_IN = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_CHANNELS_OUT = AudioFormat.CHANNEL_OUT_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;

    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    // Initialize minimum buffer size in bytes.
    private int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_IN, RECORDER_AUDIO_ENCODING);


    public void startRecorder() {

        // Initialize Audio Recorder.
        recorder = new AudioRecord(AUDIO_SOURCE, RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_IN, RECORDER_AUDIO_ENCODING, bufferSize);
        // Starts recording from the AudioRecord instance.
        recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(this::writeAudioDataToFile, "AudioRecorder Thread");
        recordingThread.start();
    }

    public void writeAudioDataToFile() {
        //Write the output audio in byte
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + "audio_record" + ".pcm";
        byte saudioBuffer[] = new byte[bufferSize];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format
            recorder.read(saudioBuffer, 0, bufferSize);
            try {
                //  writes the data to file from buffer stores the voice buffer
                os.write(saudioBuffer, 0, bufferSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        try {
            if (null != recorder) {
                isRecording = false;
                recorder.stop();
                recorder.release();
                recorder = null;
                recordingThread = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPlaying() {
        try {
            PlayShortAudioFileViaAudioTrack(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + "audio_record" + ".pcm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PlayShortAudioFileViaAudioTrack(String filePath) throws IOException{
        if (filePath==null)
            return;

        //Reading the file..
        File file = new File(filePath);
        byte[] byteData = new byte[(int) file.length()];

        FileInputStream in;
        try {
            in = new FileInputStream( file );
            in.read( byteData );
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Set and push to audio track..
        int intSize = android.media.AudioTrack.getMinBufferSize(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_OUT, RECORDER_AUDIO_ENCODING);

        AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_OUT, RECORDER_AUDIO_ENCODING, intSize, AudioTrack.MODE_STREAM);
        if (at!=null) {
            at.play();
            // Write the byte array to the track
            at.write(byteData, 0, byteData.length);
            at.stop();
            at.release();
        }
        else
            Log.d(TAG, "audio track is not initialised ");
    }

    public static double[] toDoubleArray(byte[] byteArray){
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
            //Xs[i] = i;
            Ys[i] = vals[i];
            //Xs2[i] = (double)i/Ys.length*RECORDER_SAMPLE_RATE/1000.0; // units are in kHz
        }
        return Ys;
    }

    public static byte[] toByteArray(double[] doubleArray){
        int times = Double.SIZE / Byte.SIZE;
        byte[] bytes = new byte[doubleArray.length * times];
        for(int i=0;i<doubleArray.length;i++){
            ByteBuffer.wrap(bytes, i*times, times).putDouble(doubleArray[i]);
        }
        return bytes;
    }

    public void magic() {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + "audio_record" + ".pcm");
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

        int nexpowtwo = 1;
        double[] src = toDoubleArray(byteData);
        while (true) {
            if (src.length < nexpowtwo) {
                break;
            } else {
                nexpowtwo *= 2;
            }
        }

        double[] sampleBuffer = new double[nexpowtwo];

        for (int i =0;i<src.length;i++) {
            sampleBuffer[i] = src[i];
        }

        double[] spectrum = FFT.fftMagnitude(sampleBuffer);//Spectrum(sampleBuffer);

        for(double aspectrum : spectrum) {
            System.out.println("spectrum: " + aspectrum);
        }

        double[] mel = compute(spectrum);

        for(double mels : mel) {
            System.out.println("mel: " + mels);
        }

        //double mse = MSE(mel, amel, mel.length);

    }
}


