package ru.hse.app.reader;

import ru.hse.app.domain.Point;
import ru.hse.app.parser.IParser;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class PointsReader {

    private IParser parser;

    public PointsReader(IParser parser) {
        this.parser = parser;
    }

    public List<Point> readPoints(BufferedReader reader) throws Exception {
        List<Point> points = new ArrayList<>();
        String line;
        while((line = reader.readLine()) != null) {
            Point point = parser.parse(line);
            points.add(point);
            System.out.printf("New point: %f %f %f\n", point.getT(), point.getX(), point.getY());
        }
        return points;
    }

}
