package com.company;

import java.util.HashSet;

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

        SignalReader sr = new SignalReader();
        double[] Signal1 = sr.readChannel(1);

        show(Signal1, "Signal from Channel One");

        double[] Signal2 = sr.readChannel(2);

        show(Signal2, "Signal from Channel Two");

        DoubleFFT_1D fftDo = new DoubleFFT_1D(Signal1.length);
        fftDo.realForward(Signal1);

        show(Signal1, "Signal Spectrum");

        fftDo.realForward(Signal2);

        show(Signal2, "Signal2 Spectrum");

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

        for (int i = 0; i < array.length; i++) {
            data[0][i] = i;
            data[1][i] = array[i];
        }

        ds.addSeries("signal", data);

        return ds;
    }

}
