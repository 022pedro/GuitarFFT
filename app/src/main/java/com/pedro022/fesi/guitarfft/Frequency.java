package com.pedro022.fesi.guitarfft;

public class Frequency {

    public Frequency(){};
    public Frequency(float value,int index){
        this.index=index;
        this.value=value;
    }
    public int index=0;
    public float value=0;
    public float GetFrequency(){
        return index*44100/65536.0f;
    }
}
