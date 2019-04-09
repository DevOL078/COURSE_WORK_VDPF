package ru.hse.app.visualization;

import ru.hse.app.domain.Point;
import ru.hse.app.service.PointsService;

import java.util.List;

class LoadingPointsJob {

    LoadingPointsJob() {

    }

    List<Point> loadPoints(String filePath) throws Exception {
        return PointsService.getInstance().readPoints(filePath);
    }

}
