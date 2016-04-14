package com.company;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by malll on 15.04.2016.
 */
public class SignalReader {

    private File dataFile = new File("data/2015_05_20__13_00_06.dat");
    private int ChannelNumber = 13;
    private int[] Signal;
    private int signalLenth;
    private int sampleSize = 4;

    public SignalReader() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(dataFile))) {

            byte[] signal = new byte[(int) dataFile.length()];
            dis.read(signal);

            signalLenth = signal.length / sampleSize;
            Signal = new int[signalLenth];

            ByteBuffer byteBuffer = ByteBuffer.wrap(signal);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

            for(int i = 0; i < signalLenth; i++) {
                int value = byteBuffer.getInt();
                Signal[i] = value;
            }

            System.out.println(signalLenth);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[] readChannel(int channelNumber) {

        double[] ChannelOneSignal = new double[signalLenth / ChannelNumber];

        for(int i = channelNumber - 1; i < signalLenth; i+= ChannelNumber) {
            ChannelOneSignal[i / ChannelNumber] = Signal[i];
        }
        for (int i = 0; i < ChannelOneSignal.length; i++) {
           // System.out.println(ChannelOneSignal[i]);
        }
        return ChannelOneSignal;
    }
}
