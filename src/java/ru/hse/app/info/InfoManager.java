package ru.hse.app.info;

import ru.hse.app.domain.PointVisual;

public class InfoManager {

    private static InfoManager instance = new InfoManager();
    private PointVisual currentPointVisInfo = null;

    private InfoManager(){}

    public static InfoManager getInstance() {
        return instance;
    }

    public void showPointInfo(PointVisual pointVisual) {
        if(currentPointVisInfo != null) {
            currentPointVisInfo.getInfoPane().setVisible(false);
        }
        currentPointVisInfo = pointVisual;
        currentPointVisInfo.getInfoPane().setVisible(true);
    }

    public void hideCurrentInfo() {
        currentPointVisInfo.getInfoPane().setVisible(false);
        currentPointVisInfo = null;
    }

}
