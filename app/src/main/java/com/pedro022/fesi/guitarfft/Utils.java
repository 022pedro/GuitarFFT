package com.pedro022.fesi.guitarfft;


import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static float[] amplitude(float[]x,float[] y){
        float[] mag=new float[x.length/2+1];

        x[0]=x[0]/x.length;
        y[0]=y[0]/x.length;
        mag[0]= (float) (Math.sqrt(Math.pow(x[0],2)+Math.pow(y[0],2)));
        for (int i=1;i<x.length/2;i++){
            mag[i]= (float) (Math.sqrt(Math.pow(x[i],2)+Math.pow(y[i],2)));
            mag[i]=mag[i]*2/x.length;
        }


        return mag;
    }
    public static float[] maxValues(float [] x){
        float[] maxValues=new float[1500];
        for(int i=200;i<1500;i++){
            maxValues[i]=x[i]*100000<20? 0 :x[i]*100000;
        }
        return maxValues;
    }



    public static float[] rearange(float[]full,float[] half){
        int halfindex=half.length;
        float[] result=new float[full.length*2];
        for(int i=0;i<halfindex;i++){
            result[i]=full[i];
            result[i+halfindex]=half[i];
        }
        return result;

    }


    public static List<Frequency> getMaxFreq(float[] x){
        boolean down=false;
        Frequency maxf=new Frequency();
        maxf.value=0;
        Frequency maxf2=new Frequency();
        Frequency maxf3=new Frequency();

        List<Frequency> result=new ArrayList<Frequency>();
        x[x.length-1]=0;
        for(int i=3;i<x.length-2;i++){
                if(!down && x[i]<x[i-1] && x[i-1]>x[i-2] && x[i-1]>x[i+1]){
                    result.add(new Frequency(x[i-1],i-1));
                    down=true;
                }else if(x[i]>x[i-1]&&down){down=false;

                }

        }
        result.add(maxf2);
        result.add(maxf3);
        return result;
    }


    static double gcd(double a, double b)
    {
        if (a < b)
            return gcd(b, a);

        // base case
        if (Math.abs(b) < 15)
            return a;

        else
            return (gcd(b, a -
                    Math.floor(a / b) * b));
    }


    public static DataPoint[] getData(float[] arrayToSort){
        int count = arrayToSort.length;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            DataPoint v = new DataPoint(44100*i/65536.0f, arrayToSort[i]);
            values[i] = v;
        }
        return values;
    }

    public static float[] hanningWindow(float[] signal_in, int pos, int size)
    {
        for (int i = pos; i < pos + size; i++)
        {
            int j = i - pos; // j = index into Hann window function
            signal_in[i] =(float) (signal_in[i] * 0.5 * (1.0 - Math.cos(2.0 * Math.PI * j / size)));
        }
        return signal_in;
    }



}
