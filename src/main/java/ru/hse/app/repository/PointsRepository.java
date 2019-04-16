package ru.hse.app.repository;

import ru.hse.app.domain.Point;

import java.util.List;
import java.util.stream.Collectors;

public class PointsRepository {

    private static PointsRepository instance = new PointsRepository();
    private List<Point> points;

    private PointsRepository() {}

    public void savePoints(List<Point> points) {
        this.points = points;
    }

    public List<Point> getAllPoints() {
        return points;
    }

    public List<Point> getSelectedPoints() {
        return points.stream().filter(Point::isSelected).collect(Collectors.toList());
    }

    public void deleteAllPoints() {
        points = null;
    }

    public static PointsRepository getInstance() {
        return instance;
    }

}
