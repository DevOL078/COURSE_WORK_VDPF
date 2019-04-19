package ru.hse.app;

import javafx.util.Pair;
import ru.hse.app.domain.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestCreator {

    static int r = 5;
    static double pi = Math.PI;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/main/resources/test/testLabel.txt")
                )
        );

        String line;
        List<Point> labelPoints = new ArrayList<>();
        while((line = reader.readLine()) != null) {
            System.out.println("Read: " + line);
            if(line.isEmpty()) {break;}
            String[] numbersStr = line.split(" ");
            double t = Double.parseDouble(numbersStr[0]);
            double x = Double.parseDouble(numbersStr[1]);
            double y = Double.parseDouble(numbersStr[2]);
            labelPoints.add(new Point(t, x, y));
        }

        List<Point> cickloidPoints = new ArrayList<>();
        double start = -2 * pi;
        double end = 2 * pi;
        double t = start;
        while(t <= end) {
            Pair<Double, Double> pair = cickloid(t);
            double x = pair.getKey();
            double y = pair.getValue();
            Point point = new Point(t, x, y);
            cickloidPoints.add(point);

            t += pi / 50;
        }

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("newTest.txt")
                )
        );
        cickloidPoints.forEach(p -> {
            try {
                System.out.println(p);
                writer.write(p.getT() + " " + p.getX() + " " + p.getY());
                writer.newLine();
                System.out.println("Written");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writer.close();
        reader.close();
    }

    static Pair<Double, Double> cickloid(double t) {
        double x = r * (t - Math.sin(t));
        double y = r * (1 - Math.cos(t));
        return new Pair<>(x, y);
    }

}
