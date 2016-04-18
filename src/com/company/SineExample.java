package com.company;

/**
 * Created by malll on 18.04.2016.
 */
public class SineExample {

    private final static int SAMPLERATE = 25000;

    public static double[] getSine() {
        int frequency = 100; // freq of our sine wave
        double lengthInSecs = 1;
        int samplesNum = (int) Math.round(lengthInSecs * SAMPLERATE);

        System.out.println("Samplesnum: " + samplesNum);

        double[] audioData = new double[samplesNum];
        int samplePos = 0;

        // http://en.wikibooks.org/wiki/Sound_Synthesis_Theory/Oscillators_and_Wavetables
        for (double phase = 0; samplePos < lengthInSecs * SAMPLERATE && samplePos < samplesNum; phase += (2 * Math.PI * frequency) / SAMPLERATE) {
            audioData[samplePos++] = Math.sin(phase);

            if (phase >= 2 * Math.PI)
                phase -= 2 * Math.PI;
        }

        return audioData;

    }
}
