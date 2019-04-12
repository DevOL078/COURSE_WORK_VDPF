package ru.hse.app;

import java.io.*;
import java.util.Random;

public class TestCreator {

    public static void main(String[] args) {
        Random random = new Random();
        random.setSeed(10);

        int size = 200;
        int delta = 4;

        try(BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("bigTestPoints.txt")))) {
            for(int i = 1; i <= size; ) {
                String format = "%d %f %f";
                double x = i * delta;
                double y = i * delta;
                writer.write(String.format(format, i, x, y));
                writer.newLine();
                i++;
                x = -x;
                writer.write(String.format(format, i, x, y));
                writer.newLine();
                i++;
                y = -y;
                writer.write(String.format(format, i, x, y));
                writer.newLine();
                i++;
                x = -x;
                writer.write(String.format(format, i, x, y));
                writer.newLine();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
