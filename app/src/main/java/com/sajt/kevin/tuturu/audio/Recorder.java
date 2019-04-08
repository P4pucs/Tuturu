package com.sajt.kevin.tuturu.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Recorder {

    //filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/sajt/" + getFileName();
    private String name = "";

    private static final int RECORDER_SAMPLE_RATE = 16000;
    private static final int RECORDER_CHANNELS_IN = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_CHANNELS_OUT = AudioFormat.CHANNEL_OUT_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_8BIT;

    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;

    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;


    // Initialize minimum buffer size in bytes.
    private int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_IN, RECORDER_AUDIO_ENCODING);

    public Recorder(String name) { this.name = name; }

    public Recorder() {}

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public void startRecorder() {

        // Initialize Audio Recorder.
        recorder = new AudioRecord(AUDIO_SOURCE, RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_IN, RECORDER_AUDIO_ENCODING, bufferSize);
        // Starts recording from the AudioRecord instance.
        recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(this::writeLongAudio, "AudioRecorder Thread");
        recordingThread.start();
    }

    private void writeLongAudio() {

        byte audioBuffer[] = new byte[bufferSize];

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                    getAbsolutePath() + "/sajt/alma" + getName() + ".pcm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format
            recorder.read(audioBuffer, 0, bufferSize);
            try {
                //  writes the data to file from buffer stores the voice buffer
                assert fileOutputStream != null;
                fileOutputStream.write(audioBuffer, 0, bufferSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            assert fileOutputStream != null;
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecorder() {
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

    public void startPlayingRecorder() {
        try {
            PlayAudioFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                    getAbsolutePath() + "/sajt/" + getName() + ".pcm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPlayingTemplate() {
        try {
            PlayAudioFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                    getAbsolutePath() + "/sajt/" + getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PlayAudioFile(String filePath) throws IOException {
        new Thread(()->{
            try {
                File file = new File(filePath);
                byte[] byteData = new byte[(int) file.length()];

                FileInputStream in;
                try {
                    in = new FileInputStream(file);
                    in.read(byteData);
                    in.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // Set and push to audio track..
                int intSize = android.media.AudioTrack.getMinBufferSize(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_OUT, RECORDER_AUDIO_ENCODING);

                AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_OUT,
                        RECORDER_AUDIO_ENCODING, intSize, AudioTrack.MODE_STREAM);

                if (at != null) {
                    at.play();
                    // Write the byte array to the track
                    at.write(byteData, 0, byteData.length);
                    at.stop();
                    at.release();
                } else
                    Log.d("audio recorder", "audio track is not initialised ");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void startRecorder(long time) {
        new Thread(() -> {
            recorder = new AudioRecord(AUDIO_SOURCE, RECORDER_SAMPLE_RATE, RECORDER_CHANNELS_IN, RECORDER_AUDIO_ENCODING, bufferSize);
            recorder.startRecording();
            writeShortAudio(time);
        }).start();
    }

    private void writeShortAudio(long time) {
        //Write the output audio in byte
        byte audioBuffer[] = new byte[bufferSize];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                    getAbsolutePath() + "/sajt/" + getName() + ".pcm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(time, TimeUnit.SECONDS);
        while ( System.nanoTime() < endTime ){
            recorder.read(audioBuffer, 0, bufferSize);
            try {
                assert os != null;
                os.write(audioBuffer, 0, bufferSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
            //recordingThread = null;
            assert os != null;
            os.close();
            //System.out.println("recorder DONE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


