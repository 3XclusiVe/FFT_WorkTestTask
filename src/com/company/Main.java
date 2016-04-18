package com.company;

import java.util.*;

import org.jfree.base.modules.SubSystem;
import org.jtransforms.fft.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class Main {

    public static void main(String[] args) {

        double[] sine = SineExample.getSine();
        show(sine, "Sine");

        double[] sineSpectrum = sine.clone();
        DoubleFFT_1D fftDo = new DoubleFFT_1D(sineSpectrum.length);
        fftDo.realForward(sineSpectrum);

        double[] realPart = getRealPart(sineSpectrum);
        double[] imagePart = getImagePart(sineSpectrum);

        double[] Spectrum = createSpectrum(realPart, imagePart);
        double[] AFC = createAFC(realPart, imagePart);
        double[] PFC = createPFC(realPart, imagePart);

        show(Spectrum, "Sinus Spectrum");
        show(AFC, "Sinus AFC");
        show(PFC, "Sinus PFC");

        SignalReader sr = new SignalReader();
        double[] Signal1 = sr.readChannel(2);

        show(Signal1, "Signal from Channel One");

        double[] signal1Spectrum = Signal1.clone();
        fftDo = new DoubleFFT_1D(signal1Spectrum.length);
        fftDo.realForward(signal1Spectrum);

        double[] realPart1 = getRealPart(signal1Spectrum);
        double[] imagePart1 = getImagePart(signal1Spectrum);

        double[] Spectrum1 = createSpectrum(realPart1, imagePart1);
        double[] AFCSignal1 = createAFC(realPart1, imagePart1);
        double[] PFCSignal1 = createPFC(realPart1, imagePart1);

        show(realPart1, "Signal 1 Spectrum");
        show(AFCSignal1, "Signal 1 AFC");
        show(PFCSignal1, "Signal 1 PFC");

        double[] Signal2 = sr.readChannel(2);

        show(Signal2, "Signal from Channel Two");

        double[] signal2Spectrum = Signal2.clone();
        fftDo = new DoubleFFT_1D(signal2Spectrum.length);
        fftDo.realForward(signal2Spectrum);

        double[] realPart2 = getRealPart(signal2Spectrum);
        double[] imagePart2 = getImagePart(signal2Spectrum);

        double[] Spectrum2 = createSpectrum(realPart1, imagePart1);

        double[] AFCSignal2 = createAFC(realPart2, imagePart2);
        double[] PFCSignal2 = createPFC(realPart2, imagePart2);

        show(realPart2, "Signal 2 Spectrum");
        show(AFCSignal2, "Signal 2 AFC");
        show(PFCSignal2, "Signal 2 PFC");



       //show(sineSpectrum, "Sine Spectrum");

        for(int i = 0; i < AFC.length; i++) {
           // System.out.println(sineSpectrum[i] + " " + i);
        }

        double max = 0;
        int index = 0;
        for(int i = 0; i < AFC.length; i++) {
            double absoluteValue = AFC[i];
            if( absoluteValue >= max) {
                max = absoluteValue;
                index = i;
            }
        }

        System.out.println(max + "_" + index);

    }

    private static void show(double[] array, String Title) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Charts");

                frame.setSize(1000, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                XYDataset ds = createDataset(array);
                JFreeChart chart = ChartFactory.createXYLineChart(Title,
                        "x", "y", ds, PlotOrientation.VERTICAL, true, true,
                        false);

                ChartPanel cp = new ChartPanel(chart);

                frame.getContentPane().add(cp);
            }
        });

    }

    private static XYDataset createDataset(double[] array) {

        DefaultXYDataset ds = new DefaultXYDataset();

        double[][] data = new double[2][array.length];

        double SampleRate = 25000;

        for (int i = 0; i < array.length; i++) {
            data[0][i] = ((double)i * SampleRate / 2) / (array.length);
            data[1][i] = array[i];
        }

        ds.addSeries("signal", data);

        return ds;
    }

    private static double[] convertToDb(double[] sineSpectrum) {
        double[] sineSpectruminDb = new double[sineSpectrum.length];
        for(int i = 0; i < sineSpectrum.length; i++) {
            sineSpectrum[i] = 20 * Math.log10(sineSpectrum[i] * sineSpectrum[i]);
        }
        return sineSpectruminDb;
    }

    private static double[] createSpectrum(double[] real, double[] image) {
        int size = Math.min(real.length, image.length);

        double[] magnitude = new double[size];

        for (int i = 0; i < size; i++) {
            magnitude[i] = (real[i] * real[i] + image[i] * image[i]);
        }

        return magnitude;

    }

    private static double[] createAFC(double[] real, double[] image) {
        int size = Math.min(real.length, image.length);

        double[] magnitude = new double[size];

        for (int i = 0; i < size; i++) {
            magnitude[i] = 20 * Math.log10(real[i] * real[i] + image[i] * image[i]);
        }

        return magnitude;

    }

    private static double[] createPFC(double[] real, double[] image) {
        int size = Math.min(real.length, image.length);

        double[] phase = new double[size];

        for (int i = 0; i < size; i++) {
            phase[i] = Math.atan(image[i] / real[i]);
        }

        return phase;

    }

    private static double[] getRealPart(double[] fftResult) {

        int fftSize = fftResult.length;

        double[] realPart = new double[fftSize / 2 + 1];

        if(fftResult.length % 2 == 0) {

            for (int i = 0; i < fftSize; i++) {
                if(i % 2 == 0) {
                    realPart[i / 2] = fftResult[i];
                }
            }

            realPart[fftSize / 2] = fftResult[1];

            return realPart;
        } else {
            for (int i = 0; i < fftSize; i++) {
                if(i % 2 == 0) {
                    realPart[i / 2] = fftResult[i];
                }
            }

            return realPart;
        }

    }

    private static double[] getImagePart(double[] fftResult) {

        int fftSize = fftResult.length;

        double[] imagePart = new double[fftSize / 2 + 1];

        if(fftResult.length % 2 == 0) {

            for (int i = 3; i < fftSize; i++) {
                if(i % 2 != 0) {
                    imagePart[i / 2] = fftResult[i];
                }
            }

            return imagePart;
        } else {
            for (int i = 3; i < fftSize; i++) {
                if(i % 2 != 0) {
                    imagePart[i / 2] = fftResult[i];
                }
            }

            imagePart[(fftSize - 1) / 2] = fftResult[1];

            return imagePart;
        }

    }
}
