package com.pedro022.fesi.guitarfft;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetFrequencyFromAudio extends AsyncTask <Integer, List<Frequency>, String> {
    int samplerate=65536;

    TextView[] tvarray;
    Button[] buttons;
    AudioRecord ar;

    float[] ampli;

    GraphView graph;
    SeekBar seekBar;
    LineGraphSeries mSeries1;
    int progresidle;
    Drawable[] draw;
    int countNoSound=0;
    boolean noSound=false;
    public GetFrequencyFromAudio(TextView[] tvarray, Button[]buttons, Drawable[]draw, AudioRecord ar, SeekBar seekbar, GraphView graph){
        this.draw=draw;
        this.seekBar=seekbar;
        this.tvarray=tvarray;
        this.ar=ar;
        this.graph=graph;
        this.buttons=buttons;
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(1000);
        progresidle=seekbar.getProgress();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected String doInBackground(Integer... integers) {
        float[] readAudioRealRead=new float[samplerate/2];
        float[] readAudioRealRead2=new float[samplerate/2];
        FFT fft=new FFT(samplerate);
        while(true){
           float[] readAudioReal=new float[samplerate];
           float[] readAudioImag=new float[samplerate];
           readAudioRealRead2=readAudioRealRead.clone();
           ar.read(readAudioRealRead,0,samplerate/2,AudioRecord.READ_BLOCKING);
           readAudioReal=Utils.rearange(readAudioRealRead2,readAudioRealRead);
           //Utils.hanningWindow(readAudioReal,0,samplerate);

           fft.fft(readAudioReal,readAudioImag);
           ampli=Utils.amplitude(readAudioReal,readAudioImag);
           ampli=Utils.maxValues(ampli);
           List<Frequency> freq=Utils.getMaxFreq(ampli);
            publishProgress(freq);

       }

    }
    @Override
    protected void onProgressUpdate(List<Frequency>... values) {
        if(mSeries1==null){
            mSeries1 = new LineGraphSeries<>(Utils.getData(ampli));
            graph.addSeries(mSeries1);
        }
        else
            mSeries1.resetData(Utils.getData(ampli));

        for(int i=0;i<7;i++){

            tvarray[i].setText("");
        }
        Collections.sort(values[0], new Comparator<Frequency>(){
            public int compare(Frequency s1, Frequency s2) {
                return s1.value < s2.value ? 1
                        : s1.value > s2.value ? -1
                        : 0;
            }
        });
        int size=values[0].size()<7? values[0].size():7;

        for(int i=0;i<size;i++){

            tvarray[i].setText(String.valueOf((values[0].get(i)).GetFrequency()));
        }
        //find Frequency
        double freq=0;
        if(values[0].size()>3) {
            freq = Utils.gcd((double) values[0].get(0).GetFrequency(), (double) values[0].get(1).GetFrequency());
            if (freq < 70 || freq > 370)
                freq = Utils.gcd((double) values[0].get(0).GetFrequency(), (double) values[0].get(2).GetFrequency());
            if (freq < 70 || freq > 370)
                freq = Utils.gcd((double) values[0].get(1).GetFrequency(), (double) values[0].get(2).GetFrequency());

            if (freq < 70 || freq > 370)
                freq = 0;
        }
        //chose chord
        if(freq>70&&freq<95){
            setSeekBar(freq,82);
            setColor(buttons[0]);
        }
        if(freq>95&&freq<130){
            setSeekBar(freq,110);
            setColor(buttons[1]);
        }
        if(freq>130&&freq<170){
            setSeekBar(freq,146);
            setColor(buttons[2]);
        }
        if(freq>170&&freq<220){

            setSeekBar(freq,196);
            setColor(buttons[3]);
        }
        if(freq>220&&freq<270) {
            setSeekBar(freq,246);
            setColor(buttons[4]);
        }
        if(freq>270&&freq<370){
            setSeekBar(freq,329);
            setColor(buttons[5]);
        }
        //no frequency
        if(freq==0)countNoSound++;
        else noSound=false;
        if(!noSound&&countNoSound>5){
            noSound=true;
            for (Button b:buttons) {
                b.setBackgroundColor(Color.LTGRAY);
            }
            seekBar.setProgress(progresidle);
            seekBar.setThumb(draw[3]);
        }

        //show frequency
        tvarray[6].setText(String.format("%.2f hz", freq));

    }

    private void setSeekBar(double freq,int normal) {
        seekBar.setProgress(progresidle+(int)(freq-normal)*2);
        if(Math.abs(freq-normal)<2)seekBar.setThumb(draw[0]);
        else if(Math.abs(freq-normal)<4)seekBar.setThumb(draw[1]);
        else seekBar.setThumb(draw[2]);
    }

    public void setColor(Button button){
        for (Button b:buttons) {
            b.setBackgroundColor(Color.LTGRAY);
        }
        noSound=false;
        countNoSound=0;
        button.setBackgroundColor(Color.rgb(20,109,49));
    }
}
