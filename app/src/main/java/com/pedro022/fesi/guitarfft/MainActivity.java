package com.pedro022.fesi.guitarfft;

import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

public class MainActivity extends AppCompatActivity {
    AudioRecord ar;
    SeekBar seekBar;
    TextView[] tvarray=new TextView[7];
    Button[] buttons=new Button[6];
    Drawable[] draw=new Drawable[4];

    @Override
    protected void onPause() {
        ar.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        ar.startRecording();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //seekBar
        seekBar= findViewById(R.id.seekBar2);
        seekBar.setMax(100);
        seekBar.setProgress(50);
        seekBar.setEnabled(false);
        //textArray
        tvarray[0] = findViewById(R.id.output);
        tvarray[1] = findViewById(R.id.output1);
        tvarray[2] = findViewById(R.id.output2);
        tvarray[3]= findViewById(R.id.output3);
        tvarray[4]= findViewById(R.id.output4);
        tvarray[5]= findViewById(R.id.output5);
        tvarray[6]= findViewById(R.id.output6);
        //Buttons
        buttons[5]=findViewById(R.id.buttonE4);
        buttons[4]=findViewById(R.id.buttonB3);
        buttons[3]=findViewById(R.id.buttonG3);
        buttons[2]=findViewById(R.id.buttonD3);
        buttons[1]=findViewById(R.id.buttonA2);
        buttons[0]=findViewById(R.id.buttonE2);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        //thumb Drawable
        draw[0]=getDrawable(R.drawable.green_thumb);
        draw[1]=getDrawable(R.drawable.yellow_thumb);
        draw[2]=getDrawable(R.drawable.red_thumb);
        draw[3]=getDrawable(R.drawable.grey_thumb);


        ar=new AudioRecord(MediaRecorder.AudioSource.MIC,
                44100, AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_FLOAT,65536);
        ar.startRecording();

        new GetFrequencyFromAudio(tvarray,buttons,draw,ar,seekBar,graph).execute();

    }

}
