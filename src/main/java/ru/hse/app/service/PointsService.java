package ru.hse.app.service;

import ru.hse.app.domain.Point;
import ru.hse.app.parser.ParserTXY;
import ru.hse.app.reader.PointsReader;

import java.io.*;
import java.util.List;

public class PointsService {

    private PointsReader pointsReader;
    private static PointsService instance = new PointsService();

    private PointsService() {
        pointsReader = new PointsReader(new ParserTXY());
    }

    public static PointsService getInstance() {
        return instance;
    }

    public List<Point> readPoints(String filePath) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return pointsReader.readPoints(bufferedReader);
    }

}
